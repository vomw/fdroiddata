# Block-Fusion

Block-Fusion is a small Android puzzle game built in Kotlin with Jetpack Compose.

It is a playful block-placement game inspired by classic falling-piece mechanics, with score tracking and visual skins.

## What is this project?

This repository contains a small offline mobile game. The app includes:

- a game board and piece-placement logic
- score and reset flow
- persistent high score storage
- menu and end-of-game states
- UI themes and visual customization
- a release APK build for local installation

## Project structure

- `app/` — Android app source code
- `gradle/` — Gradle wrapper files
- `build.gradle.kts` — project-level Gradle configuration
- `README.md` — project overview

## Run it locally

Requirements:

- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK 35
- an Android emulator or physical device

Steps:

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Select an emulator or connected device.
4. Run the `app` module.

## Build the APK

From the project root:

```bash
./gradlew assembleRelease
```

The generated release APK will normally be in:

- `app/build/outputs/apk/release/`

The release build is unsigned unless signing properties are provided. F-Droid signs
the APK it builds from the public source repository.

## Notes

The app works offline and does not include analytics, advertising, accounts, or
network access. The `.gitignore` avoids committing build artifacts, secrets,
signing material, and temporary files.

## License

This project is licensed under the Apache License 2.0.

See the full text in the repository license file or in the project metadata for the applicable terms.
# Block-Fusion
