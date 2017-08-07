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

    public static final String STARTING_JAVAFX_THREAD = "Starting JavaFx thread.";

    public OsgiFxBundle() {
        super();
    }

    /**
     * Called by the Blueprint manager to start the bundle.
     */
    public void startBundle()
    {
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
        Logger.getLogger("com.javatechnics.osgifx").log(Level.INFO, STARTING_JAVAFX_THREAD);
    }

    /**
     * Called by the Blueprint manager to stop the bundle. Un-registers the stage service.
     */
    public void stopBundle()
    {
        if (stageServiceRegistration != null)
        {
            stageServiceRegistration.unregister();
            Platform.runLater(() -> primaryStage.hide());
        }
    }

    /**
     * Called by the Bootstrap class once the JavaFX thread has been started (and on the JavaFx thread).
     * @param primaryStage the JavaFX Stage object.
     */
    static void setStage(Stage primaryStage)
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
