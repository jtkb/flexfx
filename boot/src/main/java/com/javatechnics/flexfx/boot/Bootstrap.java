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

package com.javatechnics.flexfx.boot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import static com.javatechnics.flexfx.boot.JavaFxExceptionMessages.JAVAFX_UNCAUGHT_EXCEPTION;

/**
 * Bundle private class that starts the JavaFx thread, returning the Stage object to the OSGi visible calling class.
 */
@Slf4j
public class Bootstrap extends Application
{

    private static Stage primaryStage;

    private static final boolean IS_FX_THREAD_RESTART = true;

    /**
     * Called by the JavaFxThread.
     *
     * @param primaryStage the primary Stage object.
     * @throws Exception thrown for runtime exception.
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        final Thread fxThread = Thread.currentThread();
        if (Platform.isFxApplicationThread())
        {
            fxThread.setUncaughtExceptionHandler((t, e) -> {
                final ClassLoader currentClassLoader = t.getContextClassLoader();
                try
                {
                    Thread.currentThread().setContextClassLoader(Bootstrap.class.getClassLoader());
                    log.error(JAVAFX_UNCAUGHT_EXCEPTION, e);
                }
                finally
                {
                    Thread.currentThread().setContextClassLoader(currentClassLoader);
                }
            });
        }
        Bootstrap.primaryStage = primaryStage;
        FlexFXBundle.setStage(primaryStage, !IS_FX_THREAD_RESTART);
    }

    /**
     * Called by a thread created when
     */
    public static void startMe()
    {
        if (primaryStage == null)
        {
            try
            {
                Platform.setImplicitExit(false);
                Bootstrap.launch();
            }
            catch (Exception exception)
            {
                FlexFXBundle.setStartupException(exception);
            }
        }
        else
        {
            FlexFXBundle.setStage(primaryStage, IS_FX_THREAD_RESTART);
        }
    }

}
