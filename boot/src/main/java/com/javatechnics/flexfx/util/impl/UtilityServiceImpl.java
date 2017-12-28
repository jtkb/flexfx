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

package com.javatechnics.flexfx.util.impl;

import com.javatechnics.flexfx.node.ControllerWrapper;
import com.javatechnics.flexfx.util.UtilityService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.lang.reflect.Field;

public final class UtilityServiceImpl implements UtilityService
{
    @Override
    public final void populateWrapper(final ControllerWrapper wrapper, final String fxmlFile) throws IOException, ClassCastException
    {
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(wrapper.getControllerClass().getClassLoader());
            final Field controllerField = wrapper.getClass().getDeclaredField(ControllerWrapper.CONTROLLER_FIELD_NAME);
            controllerField.setAccessible(true);
            final Field parentNodeField = wrapper.getClass().getDeclaredField(ControllerWrapper.NODE_FIELD_NAME);
            parentNodeField.setAccessible(true);
            final FXMLLoader loader = new FXMLLoader(wrapper.getControllerClass().getResource(fxmlFile));
            final Parent parentNode = loader.load();
            controllerField.set(wrapper, wrapper.getControllerClass().cast(loader.getController()));
            parentNodeField.set(wrapper, parentNode);
        }
        catch (IllegalAccessException | NoSuchFieldException exception)
        {
            // Safe to capture here as in full control of the ControllerWrapper source and hence field access and
            // field availability. We should never arrive here, at least not in production.
            throw new RuntimeException(exception);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(ccl);
        }
    }
}
