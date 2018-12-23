package com.javatechnics.flexfx.examples.ds;

import com.javatechnics.flexfx.examples.ds.fxml.DSController;
import com.javatechnics.flexfx.node.ControllerWrapper;
import com.javatechnics.flexfx.scene.SceneService;
import com.javatechnics.flexfx.util.UtilityService;
import javafx.scene.Scene;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import java.io.IOException;

@Component(scope = ServiceScope.SINGLETON)
public class DeclarativeServiceExample implements SceneService
{
    @Reference
    private UtilityService utilityService;

    private Scene scene;

    @Override
    public Scene getScene() throws IOException
    {
        return scene;
    }

    @Activate
    public void initialise() throws IllegalAccessException, NoSuchFieldException, IOException
    {
        ControllerWrapper<DSController> wrapper = new ControllerWrapper<>(DSController.class);
        utilityService.populateWrapper(wrapper, DSController.FXML_FILE);
        scene = new Scene(wrapper.getParent());
    }

}
