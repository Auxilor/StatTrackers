---
title: "API"
sidebar_position: 4
---

This page is for developers who want to hook into StatTrackers from their own plugin. StatTrackers is open-source, so you can read the code, depend on it, and build on top of it.

## Source code

The source code is on GitHub [here](https://github.com/Auxilor/StatTrackers).

## Adding the dependency

1. Add the Auxilor repository to your `build.gradle.kts`:
2. Add StatTrackers as a `compileOnly` dependency:

```kotlin
repositories {
    maven("https://repo.auxilor.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.willfp:StatTrackers:<version>")
}
```

The latest version available on the repo can be found [here](https://github.com/Auxilor/StatTrackers/tags).

<hr/>

## Where to go next

- **eco framework:** shared APIs live in the [eco framework](https://github.com/Auxilor/eco).
- **Make a tracker:** the config side is covered in [How to make a Stat Tracker](how-to-make-a-tracker).