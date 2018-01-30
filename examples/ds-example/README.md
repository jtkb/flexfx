### DS-Example
This example demonstrates how to offer a SceneService using OSGi declarative services.

The implementation of SceneService (DeclarativeServiceExample) annotated with `@Component` which is parsed by the `org.apache.felix.scr.annotations` plugin to generate the required declarative services XML for the bundle.

The `initialise()` method is annotated with `@Activate` and this is where the inflation of the FXML file is carried out by utilising `UtilityService.populateWrapper()` method. The parent node service of the wrapper is then returned in the `getScene()` method when called by the framework.