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

import com.javatechnics.osgifx.platform.Toolkit;
import com.javatechnics.osgifx.stage.controller.StageController;
import com.javatechnics.osgifx.util.UtilityService;
import com.javatechnics.osgifx.util.impl.UtilityServiceImpl;
import javafx.stage.Stage;
import org.osgi.framework.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the created by the OSGi framework via blueprint and triggers the starting of the JavaFx thread.
 * It essentially acts as a coordinator between the OSGi framework and the JavaFx thread.
 */
public class OsgiFxBundle implements BundleActivator
{

    private static Stage primaryStage;

    private static StageController stageController;

    public static final String STARTING_JAVAFX_THREAD = "Starting JavaFx thread.";

    public static final long JAVAFX_THREAD_STARTUP_TIMEOUT = 5;

    private static final String LOGGER_NAME = "com.javatechnics.osgifx";

    private static final CountDownLatch javaFxStartup = new CountDownLatch(1);

    private static final AtomicBoolean FX_THREAD_STARTED = new AtomicBoolean(Boolean.FALSE);

    private static final AtomicBoolean FX_THREAD_STARTUP_TIMEOUT = new AtomicBoolean(Boolean.FALSE);

    private static Exception startupException = null;

    private ServiceRegistration<UtilityService> utilityServiceServiceRegistration = null;

    private ServiceRegistration<Toolkit> toolkitServiceRegistration = null;


    public OsgiFxBundle()
    {
        super();
    }

    @Override
    public void start(final BundleContext bundleContext) throws Exception
    {
        this.startBundle(bundleContext);
    }

    @Override
    public void stop(final BundleContext bundleContext) throws Exception
    {
        this.stopBundle();
    }

    private void startBundle(final BundleContext bundleContext) throws InterruptedException, BundleException
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
        Logger.getLogger(LOGGER_NAME).log(Level.INFO, STARTING_JAVAFX_THREAD);
        javaFxStartup.await(JAVAFX_THREAD_STARTUP_TIMEOUT, TimeUnit.SECONDS);
        if (FX_THREAD_STARTED.get())
        {
            utilityServiceServiceRegistration = bundleContext.registerService(UtilityService.class, new UtilityServiceImpl(), null);
            toolkitServiceRegistration = bundleContext.registerService(Toolkit.class, () -> true, null);

        }
        else
        {
            Logger.getLogger(LOGGER_NAME).log(Level.SEVERE, String.format(JavaFxExceptionMessages.JAVAFX_THREAD_STARTUP_TIMEOUT, JAVAFX_THREAD_STARTUP_TIMEOUT));
            FX_THREAD_STARTUP_TIMEOUT.set(Boolean.TRUE);
            final BundleException bundleException = new BundleException(String.format(JavaFxExceptionMessages.JAVAFX_THREAD_STARTUP_TIMEOUT, JAVAFX_THREAD_STARTUP_TIMEOUT));
            if (startupException != null)
            {
                bundleException.initCause(startupException);
            }
            throw bundleException;
        }

    }

    private void stopBundle()
    {
        if (stageController != null)
        {
            if (utilityServiceServiceRegistration != null)
            {
                utilityServiceServiceRegistration.unregister();
                utilityServiceServiceRegistration = null;
            }
            if (toolkitServiceRegistration != null)
            {
                toolkitServiceRegistration.unregister();
                toolkitServiceRegistration = null;
            }
            stageController.stop();
            stageController = null;
            Logger.getLogger(LOGGER_NAME).log(Level.INFO, "Stopped StageController");
        }
    }

    /**
     * Called by the Bootstrap class once the JavaFX thread has been started (and on the JavaFx thread).
     *
     * @param primaryStage      the JavaFX Stage object.
     * @param isFxThreadRestart indicates if the JavaFx thread has already been started.
     */
    static void setStage(Stage primaryStage, final boolean isFxThreadRestart)
    {
        if (!FX_THREAD_STARTUP_TIMEOUT.get())
        {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(OsgiFxBundle.class.getClassLoader());
            OsgiFxBundle.primaryStage = primaryStage;

            stageController = new StageController(primaryStage);
            try
            {
                stageController.start(isFxThreadRestart);
                Logger.getLogger(LOGGER_NAME).log(Level.INFO, "Started StageController.");
                FX_THREAD_STARTED.set(Boolean.TRUE);
            }
            catch (InvalidSyntaxException e)
            {
                Logger.getLogger(LOGGER_NAME).log(Level.SEVERE, e.getMessage());
                stageController.stop();
                stageController = null;
            }
            finally
            {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
                javaFxStartup.countDown();
            }
        }
    }

    static void setStartupException(final Exception exception)
    {
        startupException = exception;
        javaFxStartup.countDown();
    }
}
