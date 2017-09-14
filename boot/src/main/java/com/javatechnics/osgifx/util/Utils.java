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

package com.javatechnics.osgifx.util;

import com.javatechnics.osgifx.node.ParentLoader;
import com.javatechnics.osgifx.scene.SceneLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Utility class.
 */
public final class Utils
{
    /**
     * Creates a Parent node by inflating the specified FXML file.
     * This also
     */
    public static final ParentLoader parentLoader = (clazz, fxmlFile) ->
    {
        final ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(clazz.getClassLoader());
        Parent parent = null;
        try {
            parent = FXMLLoader.load(clazz.getResource(fxmlFile));
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        return parent;
    };

    /**
     * Creates an object populated with an instance of the inflated FXML file.
     * It calls through to the parentLoader instance which handles the switching of the Class Loader.
     */
    public static SceneLoader sceneLoader = (clazz, fxmlFile) -> new Scene(Utils.parentLoader.loadParent(clazz, fxmlFile));

}
