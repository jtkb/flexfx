## Examples
This module contains various examples. To build all the modules from the osgifx root enable the `examples` Maven profile.
See each sub project for specific details.
#### simple-example
A very basic example that provides a SceneService object pre-populated with a Scene object.
It demonstrates how to inflate an FXML file and use it to populate a Scene object.

#### basic-node-service
A more advanced example that offers a SceneService object that itself listens for NodeService objects and can dynamically load/unload NodeServices into the scene.

The SceneService and NodeService instances inflate FXML files through the UtilityService.

#### ds-example
Another simple example this time using Declarative Services. Demonstrates the use of `UtilityService` and the `ControllerWrapper` classes. 
