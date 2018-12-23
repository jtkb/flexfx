## FlexFX

| Branch | Master | Dev |
|:--------:|:--------:|:-----:|
| Build Status | [![Build Status](https://travis-ci.org/jtkb/flexfx.svg?branch=master)](https://travis-ci.org/jtkb/flexfx) | [![Build Status](https://travis-ci.org/jtkb/flexfx.svg?branch=dev)](https://travis-ci.org/jtkb/flexfx)|

FlexFX is the combination on JavaFX with the modularity offered by the OSGi specifications and its implementations.
Frameworks such as DromblerFX which provide a rich set of GUI widgets and a standard GUI framework. FlexFX doesn't attempt to offer this but instead removes the complexity of handling JavaFX within OSGi but places fewer demands on how you create your FX components and their look.

JavaFX isn't OSGi-friendly 'out of the box'. That is it was never designed with the OSGi modularity in mind. This presents a few technical challenges that must be overcome and enforces some specific application architecture. Further technical details can be found [here](documentation/README.md)


#### Quickstart

1. Implement the [`SceneService`](boot/src/main/java/com/javatechnics/osgifx/scene/SceneService.java) interface in your bundle and offer it as an OSGi service by what ever means suited to your project. `SceneService` provides to FlexFX the root node of your scene. You can build your Scene either by instantiating Nodes through code or inflating an FXML file.
2. Deploy the FlexFX bundle.
3. Deploy your own bundle(s).

    ![Overview Image](Overview.png)

#### Examples
Code examples can be found [here](examples/README.md)

#### Java 11 & OpenJFX Support
As of Java 11 JavaFX is no longer provided as part of the JDK. It is now available as separate modules under the OpenJFX which need to be installed as an additional step. Installation can be done in a couple of ways:

1. As part of the Java runtime installation.
2. As part of the OSGi container installation. For example for apache Karaf 4.2.2 this means placing the JAR files in the `lib/jdk9plus` folder. By default the system bundle exports all the modules.