package com.javatechnics.osgifx.stage.controller;

import com.javatechnics.osgifx.scene.SceneService;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.osgi.framework.*;

import java.io.IOException;

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
                BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
                try {
                    Scene scene = context.getService((ServiceReference<SceneService>)serviceEvent.getServiceReference()).getScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
