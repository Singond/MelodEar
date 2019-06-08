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
therefore one has to run some Gradle tasks manually before updating the
Eclipse project configuration.

In the `MelodEar-javafx` project directory, run:

```
gradle eclipseJdtApt
gradle eclipseFactoryPath
gradle eclipseJdt
```

Then update the Eclipse project normally, either by `gradle eclipse`
or by clicking _Gradle_ > _Refresh Gradle Project_ within Eclipse.
