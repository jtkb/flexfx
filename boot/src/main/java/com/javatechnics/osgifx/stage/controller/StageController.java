package com.javatechnics.osgifx.stage.controller;

import com.javatechnics.osgifx.scene.SceneService;
import javafx.stage.Stage;
import org.osgi.framework.*;

public class StageController implements ServiceListener
{
    private final static String SCENE_SERVICE_FILTER = "(" + Constants.OBJECTCLASS + "="
            + SceneService.class.getName() + ")";

    private final Stage primaryStage;

    public StageController(final Stage primaryStage) {
        this.primaryStage = primaryStage;

    }

    public void start() throws InvalidSyntaxException {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        bundleContext.addServiceListener(this, SCENE_SERVICE_FILTER);
    }

    public void stop() {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        bundleContext.removeServiceListener(this);
    }

    @Override
    public void serviceChanged(final ServiceEvent serviceEvent) {

        switch (serviceEvent.getType())
        {
            case ServiceEvent.REGISTERED:
                break;
            case ServiceEvent.MODIFIED:
                break;
            case ServiceEvent.MODIFIED_ENDMATCH:
                break;
            case ServiceEvent.UNREGISTERING:
                break;
            default:
                //Should not have ended up here
        }

    }
}
