#!/bin/bash

./gradlew :core-domain:clean
./gradlew :core-domain:build
./gradlew :core-domain:publishToMavenLocal
./gradlew :member-domain:clean
./gradlew :member-domain:build
./gradlew :member-domain:publishToMavenLocal
./gradlew :member-sdk:clean
./gradlew :member-sdk:build
./gradlew :member-sdk:publishToMavenLocal
