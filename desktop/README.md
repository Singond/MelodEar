MelodEar for Desktop
====================

A melodic ear trainer application for desktop, running on JavaFX.

Build Instructions
==================

Setting up Annotation Processors
--------------------------------
This project uses [Dagger](https://dagger.dev/) for dependency injection.
Some IDEs require additional configuration in order to work smoothly with
them.

### Eclipse
Annotation processors are not properly integrated with the Buildship plugin,
therefore one has to run some Gradle tasks before updating the Eclipse
project configuration. These tasks are:

```
gradle eclipseJdtApt
gradle eclipseFactoryPath
gradle eclipseJdt
```

These tasks are now dependencies of the `eclipse` task and are expected
to be executed automatically by Buildship.

#### Known issues
Currently, editing source files causes errors to be displayed in the
Project View, which can be removed by cleaning the project.
