### Basic-Node-Service
This examples demonstrates how to provide a SceneService that itself listens for NodeServices which it dynamically loads/unloads into its scene.

It should be noted that is something of a contrived example in that NodeServices would be typically offered by other bundles. There is no benefit of offering Nodes within the same bundle as services.

The SceneService implementation (AnimatedNodeSceneService) is instantiated via Blueprint and also it registers a service listener for NodeService. In this example NodeServices are marked as 'availability=optional' otherwise the bean would not be created as the node services, in this example, are within the same bundle. It is however feasible for node services to be mandatory should an implementation choose that. Obviously in this case the SceneService would not be registered with the OSGi framework until a NodeService was available.

Two NodeServices are instantiated also via Blueprint `AnimatedNode` and `AnimatedNodeControls`.