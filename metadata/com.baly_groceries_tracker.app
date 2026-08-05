Categories:
  - Money
License: MIT
AuthorName: rw-account
SourceCode: https://github.com/rw-account/baly_groceries_tracker
IssueTracker: https://github.com/rw-account/baly_groceries_tracker/issues

AutoName: Baly Groceries Tracker

RepoType: git
Repo: https://github.com/rw-account/baly_groceries_tracker.git

Builds:
  - versionName: 1.0.0
    versionCode: 1
    commit: v1.0.0
    output: build/app/outputs/flutter-apk/app-release.apk
    srclibs:
      - flutter@3.44.0
    build:
      - $$flutter$$/bin/flutter config --no-analytics
      - $$flutter$$/bin/flutter packages pub get
      - $$flutter$$/bin/flutter build apk

AutoUpdateMode: Version v%v
UpdateCheckMode: Tags
UpdateCheckData: pubspec.yaml|version:\s.+\+(\d+)|.|version:\s(.+)\+
CurrentVersion: 1.0.0
CurrentVersionCode: 1