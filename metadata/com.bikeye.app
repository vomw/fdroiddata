Categories:
  - Navigation
License: CC BY-NC 4.0
SourceCode: https://gitlab.com/sandrinococo/bikeye
IssueTracker: https://gitlab.com/sandrinococo/bikeye/-/issues

AutoUpdateMode: Version %v
UpdateCheckMode: Tags

RepoType: git
Repo: https://gitlab.com/sandrinococo/bikeye

Builds:
  - versionName: 1.0.0
    versionCode: 1
    commit: v.1.0
    subdir: app
    script:
      - bash tool\build.sh -Environment PROD -Release