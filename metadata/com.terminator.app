Categories:
  - System
  - Development
License: GPL-3.0-or-later
AuthorName: Zaman Huseyinli
SourceCode: https://github.com/8dmusichannels-star/terminator
IssueTracker: https://github.com/8dmusichannels-star/terminator/issues

AutoName: Terminator
Summary: Terminal emulator for Android
RepoType: git
Repo: https://github.com/8dmusichannels-star/terminator.git

Builds:
  - versionName: '0.2.2'
    versionCode: 6
    commit: a1b2c3d4e5f6789012345678901234567890abcd
    subdir: app
    gradle:
      - yes
    ndk: r27c
    output: app/build/outputs/apk/release/app-release-unsigned.apk

AutoUpdateMode: Version
UpdateCheckMode: Tags
CurrentVersion: '0.2.2'
CurrentVersionCode: 6
