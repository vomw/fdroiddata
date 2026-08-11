Categories:
  - Shopping List
License: MIT
AuthorName: rw-account
AuthorEmail: whybalnbhany@gmail.com
SourceCode: https://github.com/rw-account/baly_groceries_tracker
IssueTracker: https://github.com/rw-account/baly_groceries_tracker/issues

AutoName: Baly Groceries Tracker

RepoType: git
Repo: https://github.com/rw-account/baly_groceries_tracker.git
Binaries: https://github.com/rw-account/baly_groceries_tracker/releases/download/v1.0.0/BalyGroceriesTracker-arm64-v8a-release.apk

Builds:
  - versionName: 1.0.0
    versionCode: 1
    commit: 3597b70bc9be794d5fecae31cf3de1d01574d6f3
    output: build/app/outputs/flutter-apk/app-arm64-v8a-release.apk
    srclibs:
      - flutter@3.44.8
    rm:
      - ios
      - linux
      - macos
      - web
      - windows
    prebuild:
      - git -C $$flutter$$ checkout -f 3.44.8
      - export PUB_CACHE=$(pwd)/.pub-cache
      - .flutter/bin/flutter config --no-analytics
      - .flutter/bin/flutter pub get --enforce-lockfile
    scanignore:
      - .flutter/bin/cache
    scandelete:
      - .flutter
      - .pub-cache
    build:
      - export PUB_CACHE=$(pwd)/.pub-cache
      - .flutter/bin/flutter build apk --release --split-per-abi --target-platform="android-arm64"

AllowedAPKSigningKeys: 3d168406ea989fd09452bf5801fee9460c6050165f4c930cc114e39c0bb4723c

AutoUpdateMode: Version v%v
UpdateCheckMode: Tags
VercodeOperation:
  - '%c * 10 + 2'
UpdateCheckData: pubspec.yaml|version:\s.+\+(\d+)|.|version:\s(.+)\+
CurrentVersion: 1.0.0
CurrentVersionCode: 12
