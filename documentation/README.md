# OSGi and JavaFX

First of all I must acknowledge to work of Paul Bakker upon whos work OSGiFX is based. Please see the references section below for his blog post.

The issues that must be overcome to use JavaFx within an OSGi environment:

1. Importing JavaFX packages. *

2. `ClassNotFoundException` upon starting the JavaFx thread. *

3. Single invocation of `launch()`. *

4. Coordinating access to the JavaFX stage.

5. Inflating FXML files.

\* Issues resolved by Paul Bakker and utilised in OSGiFX. Point (3) enhanced further and detailed below.

#### Importing JavaFX Packages
OSGi frameworks often allow for configuration to expose JRE packages. Apache Karaf by default exposes the JavaFX packages.

#### `ClassNotFoundException` Upon JavaFX Thread Startup
Resolved by setting the `ContextClassLoader` appropriately.

#### Single Invocation of `launch()`
By designating the JavaFX thread startup control to a single bundle removes the need for clients to concern themselves with JavaFX application start-up. Client bundles need only present the parent node of their scene graph as a service to the OSGi framework. Re-starting of the OSGiFX boot bundle is allowed but should it be unloaded then a JVM restart is required. This is due to the fact that currently once reference to the `Stage` object has been lost it cannot be re-gained.

#### Coordinating Access to the JavaFX Stage
Unlike a traditional JavaFX application which is monolithic and not dynamic an OSGi-ified JavaFX application comes with many benefits and hazards. Amongst the hazards it exposing the Stage object in an uncontrolled manner. Without coordinating access to the Stage it would be possible for a malicious or incorrectly deployed bundle to 'grab' control.
 
 To counter this the OSGi whiteboard pattern has been employed. Client bundles offer their Scene as a service which can then be placed into the Stage by the FlexFX boot bundle. Further management of the Stage is proposed for a future release that can handle multiple SceneService objects and multi-screen.
 
 (It has since been discovered when creating integration tests that it is still possible to obtain control of the Stage. The reason for this is not fully understood at this time but integration-test code indicates how this can be achieved.)
 
 #### Inflating FXML files
 Client bundles may choose to create their Scene object by inflating an FXML file. This presents a challenge within on OSGi framework as the JavaFX methods used to inflate FXML rely on the initialisation of the internal JavaFX toolkit i.e. the JavaFX thread must have been started. As bundles have no control over when they are started it is feasible for a client to attempt to inflate an FXML file before the toolkit is fully initialised. FlexFX provides two services to assist in this scenario, neither of which are made available until the FX thread and Toolkit have been started. Client bundles should take advantage of these services to avoid unexpected failure, especially upon framework start-up. Further details can be found [here](inflatingFXML.md)
 
#### JavaFx Thread Startup Timeout. 
A timeout is placed on the startup time for the FX thread to prevent the calling (OSGi) thread from being blocked indefinitely. Currently this value is hard coded but there future development may make this runtime-configurable.

[Best practices](bestpractices.md).

### References
 
 http://paulonjava.blogspot.co.uk/2014/11/making-javafx-better-with-osgi.html