Categories:
  - System
  - Development
License: GPL-3.0-or-later
AuthorName: Zaman Huseyinli
SourceCode: https://github.com/8dmusichannels-star/terminator
IssueTracker: https://github.com/8dmusichannels-star/terminator/issues
Changelog: https://github.com/8dmusichannels-star/terminator/releases

AutoName: Terminator

RepoType: git
Repo: https://github.com/8dmusichannels-star/terminator.git

Builds:
  - versionName: '0.2.2'
    versionCode: 6
    commit: v0.2.2
    subdir: app
    gradle:
      - arm64
    ndk: r27c
    binary: https://github.com/8dmusichannels-star/terminator/releases/download/v%v/app-arm64-v8a-release.apk

  - versionName: '0.2.2'
    versionCode: 6
    commit: v0.2.2
    subdir: app
    gradle:
      - armeabi
    ndk: r27c
    binary: https://github.com/8dmusichannels-star/terminator/releases/download/v%v/app-armeabi-v7a-release.apk

  - versionName: '0.2.2'
    versionCode: 6
    commit: v0.2.2
    subdir: app
    gradle:
      - x86
    ndk: r27c
    binary: https://github.com/8dmusichannels-star/terminator/releases/download/v%v/app-x86-release.apk

  - versionName: '0.2.2'
    versionCode: 6
    commit: v0.2.2
    subdir: app
    gradle:
      - x86_64
    ndk: r27c
    binary: https://github.com/8dmusichannels-star/terminator/releases/download/v%v/app-x86_64-release.apk

AllowedAPKSigningKeys:
  - 2ceccbe571e76d60d46776d59c58158dbf46349e81eb3b0d700f37ea2f9e3434

AutoUpdateMode: Version
UpdateCheckMode: Tags
CurrentVersion: '0.2.2'
CurrentVersionCode: 6