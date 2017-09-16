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

package com.javatechnics.osgifx.boot;

import com.javatechnics.osgifx.stage.controller.StageController;
import javafx.stage.Stage;
import org.osgi.framework.InvalidSyntaxException;

import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the created by the OSGi framework via blueprint and triggers the starting of the JavaFx thread.
 * It essentially acts as a coordinator between the OSGi framework and the JavaFx thread.
 */
public class OsgiFxBundle
{

    private static Stage primaryStage;

    private static StageController stageController;

    public static final String STARTING_JAVAFX_THREAD = "Starting JavaFx thread.";

    private static final String LOGGER_NAME = "com.javatechnics.osgifx";

    public OsgiFxBundle() {
        super();
    }

    /**
     * Called by the Blueprint manager to start the bundle.
     */
    public void startBundle() {
        /*
        Bootstrap.startMe() must be called from another thread with it's context class loader set to that of
        this class. The class loader is required because Application.launch() method calls the classloader of the calling
        thread (not the class!). Start in another thread is also required otherwise an unexpected exception is thrown
        from further down in the JavaFX core code. (reason not fully understood at this time).
         */
        Executors.defaultThreadFactory().newThread(() ->
        {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            Bootstrap.startMe();

        }).start();
        Logger.getLogger(LOGGER_NAME).log(Level.INFO, STARTING_JAVAFX_THREAD);
    }

    /**
     * Called by the Blueprint manager to stop the bundle. Un-registers the stage service.
     */
    public void stopBundle() {
        if (stageController != null) {
            stageController.stop();
            stageController = null;
            Logger.getLogger(LOGGER_NAME).log(Level.INFO, "Stopped StageController");
        }
    }

    /**
     * Called by the Bootstrap class once the JavaFX thread has been started (and on the JavaFx thread).
     *
     * @param primaryStage the JavaFX Stage object.
     * @param isFxThreadRestart indicates if the JavaFx thread has already been started.
     */
    static void setStage(Stage primaryStage, final boolean isFxThreadRestart) {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(OsgiFxBundle.class.getClassLoader());
        OsgiFxBundle.primaryStage = primaryStage;

        stageController = new StageController(primaryStage);
        try {
            stageController.start(isFxThreadRestart);
            Logger.getLogger(LOGGER_NAME).log(Level.INFO, "Started StageController.");
        } catch (InvalidSyntaxException e) {
            Logger.getLogger(LOGGER_NAME).log(Level.SEVERE, e.getMessage());
            stageController.stop();
            stageController = null;
        }

        Thread.currentThread().setContextClassLoader(currentClassLoader);

    }

}
