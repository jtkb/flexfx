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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Bundle private class that starts the JavaFx thread, returning the Stage object to the OSGi visible calling class.
 */
public class Bootstrap extends Application {

    private static Stage primaryStage;

    private static String APPLICATION_LAUNCH_EXCEPTION = "Application launch must not be called more than once";

    private static final boolean IS_FX_THREAD_RESTART = true;

    /**
     * Called by the JavaFxThread.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Bootstrap.primaryStage = primaryStage;
        OsgiFxBundle.setStage(primaryStage, !IS_FX_THREAD_RESTART);
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
            OsgiFxBundle.setStage(primaryStage, IS_FX_THREAD_RESTART);
        }
    }

}
