/*
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Copyright Kerry Billingham - 2017
 *    @author Kerry Billingham
 */

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

/**
 * A bundle private class that controls access to the Stage object.
 * This class is instantiated after the JavaFx has been started and registers a listener of
 * {@link com.javatechnics.osgifx.scene.SceneService}. This is done to prevent uncontrolled access to the Stage object.
 * Currently the controller accepts the first SceneService that it receives but future revisions will make use of
 * {@link com.javatechnics.osgifx.stage.management.StageManager} objects for add security.
 */
public class StageController implements ServiceListener
{
    // SceneService filter
    private final static String SCENE_SERVICE_FILTER = "(" + Constants.OBJECTCLASS + "="
            + SceneService.class.getName() + ")";
    private static final String LOGGER_NAME = "StageController";

    private final Stage primaryStage;

    private boolean isFxThreadRestart = true;
    private boolean hasBeenVisible = false;
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
                    if (!isFxThreadRestart && !hasBeenVisible) {
                        primaryStage.initStyle(stageStyle);
                    }
                    primaryStage.show();
                    hasBeenVisible = true;
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
