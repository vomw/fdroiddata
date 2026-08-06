Categories:
  - Money

License: MIT

AuthorName: rw-account
AuthorEmail: whybalnbhany@gmail.com

SourceCode: https://github.com/rw-account/baly_groceries_tracker
IssueTracker: https://github.com/rw-account/baly_groceries_tracker/issues

AutoName: Baly Groceries Tracker

RepoType: git
Repo: https://github.com/rw-account/baly_groceries_tracker.git

Builds:
  - versionName: 1.0.0
    versionCode: 1
    commit: 8f0af9e27b1bbce77eda49caf0062b5020c04213

    output: build/app/outputs/flutter-apk/app-release.apk

    srclibs:
      - flutter@3.44.0

    rm:
      - ios
      - linux
      - macos
      - web
      - windows

    prebuild:
      - git -C $$flutter$$ checkout -f 3.44.0
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

AutoUpdateMode: Version v%v

UpdateCheckMode: Tags

UpdateCheckData: pubspec.yaml|version:\s.+\+(\d+)|.|version:\s(.+)\+

CurrentVersion: 1.0.0

CurrentVersionCode: 1