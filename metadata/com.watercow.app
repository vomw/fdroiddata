Categories:
  - Health & Fitness
License: MIT
AuthorName: Water Cow
SourceCode: https://github.com/kishor-23/Water-Cow.git
IssueTracker: https://github.com/kishor-23/Water-Cow/issues

AutoUpdateMode: Version v%v
UpdateCheckMode: Tags ^v[0-9.]+$

Summary: Hydration tracking app with expressive cow mascot
Description: |-
  Water Cow is a feature-packed hydration tracking app with custom reminders,
  expressive mascot reactions, custom notification sounds, and a native Android home screen widget.

Builds:
  - versionName: 1.0.0
    versionCode: 1
    commit: v1.0.0
    subdir: android
    sudo:
      - apt-get update
      - apt-get install -y nodejs npm
    init:
      - npm ci --legacy-peer-deps
    gradle:
      - yes

AutoUpdate:
  Version: 1.0.0
  VersionCode: 1
