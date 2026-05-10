#!/bin/sh
set -eu

npm install --ignore-scripts

# Debian trixie build images ship Java 21; patch vendored RN plugin toolchains from 17 to 21.
perl -0pi -e 's/jvmToolchain\(17\)/jvmToolchain(21)/g' \
  node_modules/@react-native/gradle-plugin/react-native-gradle-plugin/build.gradle.kts \
  node_modules/@react-native/gradle-plugin/settings-plugin/build.gradle.kts \
  node_modules/@react-native/gradle-plugin/shared/build.gradle.kts \
  node_modules/@react-native/gradle-plugin/shared-testutil/build.gradle.kts
