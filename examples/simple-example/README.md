## Simple Example
This example displays the most simplest of use cases, an FXML file is loaded and inflated and used to populate a Scene
object which is then offered via a scene service.

Note that there is also a Controller object associated with the FXML
file but this is instantiated automatically as part of the FXML load. In
all other respects the controller and FXML file (layout) are written
in the standard FXML way.

Due to the nature of OSGi the FXML file is bundle-private and
not necessarily visible to the thread calling the bundle activator. 
The thread calling the activator is implementation specific therefore
suitable Class Loader switching must take place. This example takes
advantage of the `Utils` class in the `boot` module which has
functional implementations that can load an FXML with the required 
Class Loader switching.

### Installing In Karaf
Install the simple-example feature with the following command:

`repo-add mvn:com.javatechnics.flexfx/simple-example/1.0.0-SNAPSHOT/xml/features`