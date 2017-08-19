package com.javatechnics.osgifx.boot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Bootstrap extends Application {

    private static Stage primaryStage;

    private static String APPLICATION_LAUNCH_EXCEPTION = "Application launch must not be called more than once";

    /**
     * Called by the JavaFxThread.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Bootstrap.primaryStage = primaryStage;
        OsgiFxBundle.setStage(primaryStage);
    }

    /**
     * Called by a thread created when
     */
    public static void startMe()
    {
        if (primaryStage == null) {
            try {
                Platform.setImplicitExit(false);
                Bootstrap.launch();
            } catch (IllegalStateException exception) {
                // Catch the exception here - perhaps log - the JavaFX main thread has already been started?
                System.out.println(exception.getMessage());
            }
        }
        else
        {
            OsgiFxBundle.setStage(primaryStage);
        }
    }

}
