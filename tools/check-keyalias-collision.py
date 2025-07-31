#!/usr/bin/env python3

"""Test that the appid could always be the actual keyalias.

Historically, fdroid keystores have used a substring of the checksum of
the Application ID for the key alias. Java keystores should support all
characters, so the full Application ID should serve as the unique ID
here as it does everywhere else. This adds checks to make sure that
actually works in practice, based on the Java Keystore recommendations.

https://docs.oracle.com/javase/7/docs/api/java/security/KeyStore.html

This is a small step towards the %signing server milestone. This should
make it a lot easier to remove this technical debt, e.g. custom keyalias
generation algorithm.

"""

import glob
import hashlib
import json
import os
import urllib.request
import shutil
import subprocess
import sys
import tempfile


def generate_keyalias(s):
    m = hashlib.md5()
    m.update(s.encode())
    return m.hexdigest()[:8]


metadata_files = glob.glob("metadata/*.yml")

CODEQUALITY_REPORT = list()
CHECK_NAME = os.path.basename(__file__)

if not metadata_files:
    print("No metadata files found!")
    sys.exit(1)

ORIG_KEYALIAS = "start_alias"
# Generate throwaway keystore to test Application IDs as keystore aliases
keystore_dir = tempfile.mkdtemp()
keystore = [
    '-keystore',
    os.path.join(keystore_dir, "keystore.p12"),
    '-storepass',
    "insecurepassword",
]
# taken from fdroidserver.common.genkeystore()
cmd = [
    "keytool",
    "-genkey",
    "-alias",
    ORIG_KEYALIAS,
    "-keyalg",
    "RSA",
    "-keysize",
    "4096",
    "-sigalg",
    "SHA256withRSA",
    "-validity",
    "10000",
    "-storetype",
    "pkcs12",
    "-dname",
    "CN=Birdman, OU=Cell, O=Alcatraz, L=Alcatraz, S=California, C=US",
    "-J-Duser.language=en",
] + keystore
print(' '.join(cmd))
subprocess.check_call(cmd)

errors = 0
keyaliases = dict()
for f in metadata_files:
    appid = os.path.basename(f)[:-4]

    # Eventually the appid should always be the actual keyalias. This
    # tests to make sure that will work.
    cmd = ['keytool', '-keyclone', '-alias', ORIG_KEYALIAS, '-dest', appid] + keystore
    if len(sys.argv) == 1 or appid in sys.argv:
        print(' '.join(cmd))
        subprocess.check_call(cmd)

    keyalias = generate_keyalias(appid)
    if keyalias in keyaliases:
        errors += 1
        original = appid
        conflicting = keyaliases[keyalias]
        try:
            url = f'https://f-droid.org/api/v1/packages/{appid}'
            r = urllib.request.Request(url, method='HEAD')
            with urllib.request.urlopen(r) as response:
                if response.status == 200:
                    original = keyaliases[keyalias]
                    conflicting = appid
        except urllib.error.URLError:
            pass
        msg = f"{conflicting} keyalias conflicts with {original}"
        path = f'metadata/{conflicting}.yml'
        print(msg)
        CODEQUALITY_REPORT.append(
            {
                "description": msg,
                "check_name": CHECK_NAME,
                "fingerprint": CHECK_NAME + keyalias + f + path,
                "severity": "blocker",
                "location": {"path": path, "lines": {"begin": 0}},
            }
        )
    keyaliases[keyalias] = appid

# it is recommended not to use aliases in a KeyStore that only differ in case.
# https://docs.oracle.com/javase/7/docs/api/java/security/KeyStore.html
original_keyaliases = set(keyaliases.keys())
lowercase_keyaliases = set()
for keyalias in original_keyaliases:
    lowercase_keyaliases.add(keyalias.lower())
for keyalias in original_keyaliases ^ lowercase_keyaliases:
    errors += 1
    CODEQUALITY_REPORT.append(
        {
            "description": f"Keyalias differs only by case: {keyalias}",
            "check_name": CHECK_NAME,
            "fingerprint": CHECK_NAME + keyalias,
            "severity": "blocker",
            "location": {"path": f"metadata/{keyalias}.yml", "lines": {"begin": 0}},
        }
    )

with open(f"{CHECK_NAME}.json", "w") as fp:
    json.dump(CODEQUALITY_REPORT, fp)

shutil.rmtree(keystore_dir, ignore_errors=True)
sys.exit(errors)
