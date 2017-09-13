package com.javatechnics.osgifx.examples.simple;

import com.javatechnics.osgifx.scene.SceneService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class SimpleExampleActivator implements BundleActivator
{
    private ServiceRegistration<SceneService> sceneServiceServiceRegistration;

    @Override
    public void start(final BundleContext bundleContext) throws Exception
    {
        final SimpleSceneService simpleSceneService = new SimpleSceneService();

        // An alternative is to create a specific initialise method of the service. This would allow a fast-fail
        // and prevent the service being made available.
        // simpleSceneService.initialise();

        sceneServiceServiceRegistration = bundleContext.registerService(SceneService.class, simpleSceneService, null);
    }

    @Override
    public void stop(final BundleContext bundleContext) throws Exception
    {
        sceneServiceServiceRegistration.unregister();
    }
}
