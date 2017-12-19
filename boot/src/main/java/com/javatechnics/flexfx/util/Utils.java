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

package com.javatechnics.flexfx.util;

import com.javatechnics.flexfx.node.ControllerWrapper;
import com.javatechnics.flexfx.node.ParentLoader;
import com.javatechnics.flexfx.scene.SceneLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Utility class.
 */
public final class Utils
{
    /**
     * Creates a Parent node by inflating the specified FXML file.
     * This also handles the switching of the class loader.
     */
    public static final ParentLoader parentLoader = (clazz, fxmlFile) ->
    {
        final ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
        Parent parent = null;
        try
        {
            parent = FXMLLoader.load(clazz.getResource(fxmlFile));
        } finally
        {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        return parent;
    };

    /**
     * Creates an object populated with an instance of the inflated FXML file.
     * It calls through to the parentLoader instance which handles the switching of the Class Loader.
     */
    public static SceneLoader sceneLoader = (clazz, fxmlFile) -> new Scene(Utils.parentLoader.loadParent(clazz, fxmlFile));

    /**
     * Inflates the specified FXML file returning the parent node and controller class in an immutable object.
     *
     * @param clazz    The controller class specified in the FXML file.
     * @param fxmlFile the specified FXML file to load and inflate.
     * @param <T>      The controller type as specified in the FXML file.
     * @return ControllerWrapper containing the controller and parent node.
     * @throws IOException thrown if the specified FXML file cannot be found.
     */
    public static <T> ControllerWrapper<T> getWrapper(Class<T> clazz, String fxmlFile) throws IOException, ClassCastException, NoSuchFieldException, IllegalAccessException
    {
        ControllerWrapper<T> wrapper = null;
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        try
        {
            Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
            final Field controllerField = wrapper.getClass().getDeclaredField(ControllerWrapper.CONTROLLER_FIELD_NAME);
            controllerField.setAccessible(true);
            final Field parentNodeField = wrapper.getClass().getDeclaredField(ControllerWrapper.NODE_FIELD_NAME);
            parentNodeField.setAccessible(true);
            FXMLLoader loader = new FXMLLoader(clazz.getResource(fxmlFile));
            Parent parentNode = loader.load();
            wrapper = new ControllerWrapper<>(clazz);
            controllerField.set(wrapper, wrapper.getControllerClass().cast(loader.getController()));
            parentNodeField.set(wrapper, parentNode);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(ccl);
        }

        return wrapper;
    }
}
