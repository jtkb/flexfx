package com.javatechnics.osgifx.examples.simple;

import com.javatechnics.osgifx.scene.SceneService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class SimpleExampleActivator implements BundleActivator
{

    private ServiceRegistration<SceneService> sceneServiceServiceRegistration;
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        sceneServiceServiceRegistration = bundleContext.registerService(SceneService.class, new SimpleSceneService(), null);
    }

    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        sceneServiceServiceRegistration.unregister();
    }
}
