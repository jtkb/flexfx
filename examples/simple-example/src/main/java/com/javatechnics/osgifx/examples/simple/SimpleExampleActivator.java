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
