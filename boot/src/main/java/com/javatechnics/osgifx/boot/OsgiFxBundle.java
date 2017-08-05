package com.javatechnics.osgifx.boot;

import com.javatechnics.osgifx.stage.StageService;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class OsgiFxBundle implements StageService {

    private static Stage primaryStage;

    private static ServiceRegistration stageServiceRegistration;

    private static StageService stageService;

    public OsgiFxBundle() {
        super();
    }

    /**
     * Called by the Blueprint manager to start the bundle.
     */
    public void startBundle()
    {
        Executors.defaultThreadFactory().newThread(() ->
        {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            Bootstrap.startMe();

        }).start();
        Logger.getLogger("com.javatechnics.osgifx").log(Level.INFO, "Starting JavaFx thread.");
    }

    public void stopBundle()
    {
        if (stageServiceRegistration != null)
        {
            stageServiceRegistration.unregister();
            Platform.runLater(() -> primaryStage.hide());
        }
    }

    /**
     * Called by the JavaFXBootstrap class once the JavaFX thread has been started and Stage object is
     * available.
     * @param primaryStage the JavaFX Stage object.
     */
    public static void setStage(Stage primaryStage)
    {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(OsgiFxBundle.class.getClassLoader());
        OsgiFxBundle.primaryStage = primaryStage;

        BundleContext bc = FrameworkUtil.getBundle(OsgiFxBundle.class).getBundleContext();
        Properties metaData = new Properties();
        metaData.setProperty("name", "javafx-stage-service");

        stageService = new OsgiFxBundle();

        stageServiceRegistration = bc.registerService(StageService.class, stageService, (Dictionary) metaData);
        Thread.currentThread().setContextClassLoader(currentClassLoader);
    }

    @Override
    public Stage getStage() {
        return OsgiFxBundle.primaryStage;
    }


}
