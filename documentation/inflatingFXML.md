### Inflating FXML files

In a traditional JavaFX application and FXML file is inflated typically in this manner:

```java
public class MyJavaFXApplication extends Application
{
    private MyApplicationFXMLController controller;
    
    public static void main(String [] args)
    {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        FXMLLoader loader = FXMLLoader(MyJavaFXApplication.class.getResource("my_fxml_layout.fxml"));
        Parent root = loader.load();
        controller = (MyApplicationFXMLController)loader.getController();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
```
In OSGiFX your bundles could do something similar when they are deployed however `FXMLLoader` internally relies upon the JavaFX toolkit to have been initialised. OSGiFX removes the need for your bundles to deal with the toolkit start-up so how can you reliably inflate FXML files if you do not know what state to toolkit is in? OSGi services solves this problem. If your bundles inflate FXML files then it is recommended that you take advantage of either [UtilityService](../boot/src/main/java/com/javatechnics/osgifx/util/UtilityService.java) or [Toolkit](../boot/src/main/java/com/javatechnics/osgifx/platform/Toolkit.javaMyApplicationFXMLController). `UtilityService` provides a full and safe means of FXML inflation and access to the FXML controller. `Toolkit` is useful if your bundles just need notification that the Toolkit is initialised. Both services are published at the same time. Further, the FXML file itself may not necessarily be exposed in a package that is exported by the bundle presenting a problem for calling context which is not that of the bundle. 

#### Using `UtilityService`

Using Declarative Services a typical usage of the `UtilityService` is as follows:

```java
@Component
public class MySceneServiceImpl implements SceneService
{
    @Reference
    private UtilityService utilityService;
    
    private Scene scene;
    
    @Activate
    public void initialise() throws IllegalStateException, NoSuchFieldException, IOExcpetion
    {
        ControllerWrapper<MyFXMLController> wrapper = new ControllerWrapper<>(MyFXMLController.class);
        utilityService.populateWrapper(wrapper, "my_fxml_layout.fxml");
        scene = new Scene(wrapper.getParent());
    }
    
    @Override
    public getScene() throws java.io.IOException
    {
        return scene;
    }
}
```

The `UtilityService` is injected into the DS component thus `MySceneServiceImpl` is not instantiated until it is available. To inflate the FXML the `initialise()` method takes advantage of the `ControllerWrapper` generic class which provides a type-safe way of obtaining the FXML controller. Passing the `ControllerWrapper` class to the `UtilityService.populateWrapper()` method which takes care of the context switching to inflate the FXML and obtain the instance of the controller. A `Scene` object is then created using the Parent node from the wrapper and this scene object is offered via the `getScene()` method to OSGiFX.

A full example can be seen in the `ds-example` module in the `examples` directory.