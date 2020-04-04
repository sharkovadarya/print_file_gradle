# printfile 
Gradle plugin to print file contents with marked lines skipped.

Usage:

1. Execute `./gradlew clean build publishToMavelLocal`.

2. Now it's possible to add this plugin to other projects. In order to to so, add
```
plugins {
    id("ru.hse.spb.sharkova.printfile") version "0.0.1"
}
```
to your `build.gradle.kts` file.


Note that your `settings.gradle.kts` file must contain the following:
```
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}
```

3. To configure the plugin, add the following to your `build.gradle.kts` file:
```
printfile { 
    enabled = true
    filename = "your/filename.txt"
}
```

If you wish to use command line to supply the file name, launch the plugin with an additional `filename` property specified:

`gradle printfile -Pfilename="your/filename.txt"`

4. To disable the plugin, set `enabled` (see above) to false.
