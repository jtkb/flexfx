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

import com.javatechnics.flexfx.platform.Toolkit;
import com.javatechnics.flexfx.stage.controller.StageController;
import com.javatechnics.flexfx.util.UtilityService;
import com.javatechnics.flexfx.util.impl.UtilityServiceImpl;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is the created by the OSGi framework and triggers the starting of the JavaFx thread.
 * It essentially acts as a coordinator between the OSGi framework and the JavaFx thread.
 */
@Slf4j
public class FlexFXBundle implements BundleActivator
{

    private static Stage primaryStage;

    private static StageController stageController;

    public static final String STARTING_JAVAFX_THREAD = "Starting JavaFx thread.";

    public static final long JAVAFX_THREAD_STARTUP_TIMEOUT = 5;

    private static final CountDownLatch javaFxStartup = new CountDownLatch(1);

    private static final AtomicBoolean FX_THREAD_STARTED = new AtomicBoolean(Boolean.FALSE);

    private static final AtomicBoolean FX_THREAD_STARTUP_TIMEOUT = new AtomicBoolean(Boolean.FALSE);

    private static Exception startupException = null;

    private ServiceRegistration<UtilityService> utilityServiceServiceRegistration = null;

    private ServiceRegistration<Toolkit> toolkitServiceRegistration = null;


    public FlexFXBundle()
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
        log.info(STARTING_JAVAFX_THREAD);
        javaFxStartup.await(JAVAFX_THREAD_STARTUP_TIMEOUT, TimeUnit.SECONDS);
        if (FX_THREAD_STARTED.get())
        {
            utilityServiceServiceRegistration = bundleContext.registerService(UtilityService.class, new UtilityServiceImpl(), null);
            toolkitServiceRegistration = bundleContext.registerService(Toolkit.class, () -> true, null);

        }
        else
        {
            FX_THREAD_STARTUP_TIMEOUT.set(Boolean.TRUE);
            final BundleException bundleException;
            if (startupException != null)
            {
                bundleException = new BundleException(startupException.getMessage());
                bundleException.initCause(startupException);
                log.error(JavaFxExceptionMessages.JAVAFX_THREAD_STARTUP_FAILED);
            }
            else
            {
                bundleException = new BundleException(String.format(JavaFxExceptionMessages.JAVAFX_THREAD_STARTUP_TIMEOUT, JAVAFX_THREAD_STARTUP_TIMEOUT));
                log.error(String.format(JavaFxExceptionMessages.JAVAFX_THREAD_STARTUP_TIMEOUT, JAVAFX_THREAD_STARTUP_TIMEOUT));
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
            log.info("Stopped StageController.");
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
            Thread.currentThread().setContextClassLoader(FlexFXBundle.class.getClassLoader());
            FlexFXBundle.primaryStage = primaryStage;

            stageController = new StageController(primaryStage);
            try
            {
                stageController.start(isFxThreadRestart);
                FX_THREAD_STARTED.set(Boolean.TRUE);
                log.info("Started StageController.");
            }
            catch (InvalidSyntaxException e)
            {
                stageController.stop();
                stageController = null;
                log.error("Exception when setting Stage object into FlexFXBundle.", e);
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
