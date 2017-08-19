package com.javatechnics.osgifx.boot;

import com.javatechnics.osgifx.stage.controller.StageController;
import javafx.stage.Stage;
import org.osgi.framework.InvalidSyntaxException;

import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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
    public void stopBundle()
    {
        if (stageController != null) {
            stageController.stop();
            stageController = null;
            Logger.getLogger(LOGGER_NAME).log(Level.INFO,"Stopped StageController");
        }
    }

    /**
     * Called by the Bootstrap class once the JavaFX thread has been started (and on the JavaFx thread).
     *
     * @param primaryStage the JavaFX Stage object.
     */
    static void setStage(Stage primaryStage) {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(OsgiFxBundle.class.getClassLoader());
        OsgiFxBundle.primaryStage = primaryStage;

        stageController = new StageController(primaryStage);
        try {
            stageController.start();
            Logger.getLogger(LOGGER_NAME).log(Level.INFO, "Started StageController.");
        } catch (InvalidSyntaxException e) {
            Logger.getLogger(LOGGER_NAME).log(Level.SEVERE, e.getMessage());
            stageController.stop();
            stageController = null;
            e.printStackTrace();
        }

        Thread.currentThread().setContextClassLoader(currentClassLoader);

    }

}
