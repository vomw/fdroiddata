Categories:
  - System
  - Development
License: GPL-3.0-or-later
AuthorName: Zaman Huseyinli
SourceCode: https://github.com/8dmusichannels-star/terminator
IssueTracker: https://github.com/8dmusichannels-star/terminator/issues

AutoName: Terminator
Summary: Terminal emulator for Android

Description: |-
    Terminator is a terminal emulator for Android with multiple session
    support, Material UI theming, AMOLED black support, custom terminal
    color schemes (including Nord), and support for xterm256-color,
    VT100, and ANSI terminal types. Sessions can be file-based or
    command-arg based, with optional root session support. The native
    terminal backend is implemented in C as a separate module
    (terminal-emulator). Includes resizable terminal text, background
    blur, custom fonts, and keyboard shortcut/keymapper support.

RepoType: git
Repo: https://github.com/8dmusichannels-star/terminator.git

Builds:
  - versionName: '0.2.2'
    versionCode: 6
    commit: v0.2.2
    subdir: app
    gradle:
      - yes
    # namespace/applicationId: com.terminator.app (confirmed in build.gradle.kts)
    # minSdk 33, compileSdk/targetSdk 35, uses Kotlin/Compose
    # The terminal-emulator module is C source code (JNI), included via
    # implementation(project(":terminal-emulator")) in dependencies,
    # so an NDK toolchain is required. Verify the actual NDK version from
    # local.properties or the CI workflow and set it below:
    ndk: r27c
    output: app/build/outputs/apk/release/app-release-unsigned.apk

AutoUpdateMode: Version
UpdateCheckMode: Tags
CurrentVersion: '0.2.2'
CurrentVersionCode: 6
