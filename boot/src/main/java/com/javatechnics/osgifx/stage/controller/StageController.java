package com.javatechnics.osgifx.stage.controller;

import com.javatechnics.osgifx.scene.SceneService;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.osgi.framework.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StageController implements ServiceListener
{
    // SceneService filter
    private final static String SCENE_SERVICE_FILTER = "(" + Constants.OBJECTCLASS + "="
            + SceneService.class.getName() + ")";
    private static final String LOGGER_NAME = "StageController";

    private final Stage primaryStage;

    private boolean isFxThreadRestart = true;
    private boolean hasBeenvisible = false;
    private final Object serviceListenerLock = new Object();

    public StageController(final Stage primaryStage) {
        this.primaryStage = primaryStage;

    }

    public void start(final boolean isFxThreadRestart) throws InvalidSyntaxException {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        this.isFxThreadRestart = isFxThreadRestart;
        synchronized (serviceListenerLock) {
            // Before starting listener see if any SceneService objects have already been registered
            ServiceReference<SceneService> sceneServiceServiceReference = bundleContext.getServiceReference(SceneService.class);

            if (sceneServiceServiceReference != null) {
                serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, sceneServiceServiceReference));
            }
            // Register the scene service listener.
            bundleContext.addServiceListener(this, SCENE_SERVICE_FILTER);
        }
    }

    public void stop() {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        bundleContext.removeServiceListener(this);
        Platform.runLater(() -> {
            if (primaryStage.getScene() != null)
            {
                primaryStage.setScene(null);
                primaryStage.hide();
            }
        });
    }

    @Override
    public void serviceChanged(final ServiceEvent serviceEvent) {

        final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        synchronized (serviceListenerLock) {
            final ServiceReference<SceneService> sceneServiceServiceReference = (ServiceReference<SceneService>) serviceEvent.getServiceReference();
            switch (serviceEvent.getType()) {
                case ServiceEvent.REGISTERED:
                    launchScene(context, sceneServiceServiceReference);
                    break;
                case ServiceEvent.MODIFIED:
                    break;
                case ServiceEvent.MODIFIED_ENDMATCH:
                    break;
                case ServiceEvent.UNREGISTERING:
                    removeScene(context, sceneServiceServiceReference);
                    break;
                default:
                    //Should not have ended up here
            }
        }
    }

    private void launchScene(final BundleContext context, final ServiceReference<SceneService> sceneServiceReference)
    {
        try {
            final SceneService sceneService = context.getService(sceneServiceReference);
            final Scene scene = sceneService.getScene();
            final StageStyle stageStyle = sceneService.getPreferredStageStyle();
            Platform.runLater(() -> {
                if (primaryStage.getScene() == null)
                {
                    primaryStage.setScene(scene);
                    if (!isFxThreadRestart && !hasBeenvisible) {
                        primaryStage.initStyle(stageStyle);
                    }
                    primaryStage.show();
                    hasBeenvisible = true;
                }
            });
        } catch (IOException e) {
            Logger.getLogger(LOGGER_NAME).log(Level.SEVERE, e.getLocalizedMessage());
        }

    }

    private void removeScene(final BundleContext context, final ServiceReference<SceneService> sceneServiceServiceReference)
    {
        try {
            final Scene scene = context.getService(sceneServiceServiceReference).getScene();
            Platform.runLater(() -> {
                if (primaryStage.getScene() != null && primaryStage.getScene() == scene)
                {
                    primaryStage.setScene(null);
                    primaryStage.hide();
                }
            });
        } catch (IOException e) {
            Logger.getLogger(LOGGER_NAME).log(Level.SEVERE, e.getLocalizedMessage());
        }
    }

}
