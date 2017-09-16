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

package com.javatechnics.osgifx.scene;

import javafx.scene.Scene;

import java.io.IOException;

@FunctionalInterface
public interface SceneLoader
{
    /**
     * The purpose of this method is to allow the loading of bundle-private FXML files and the population a Scene object
     * with the inflated file. Implementations should handle the switch to and from the appropriate Class Loaader.
     * @param clazz a Class object which is in the same bundle as the FXML file and visible to the bundle's Class Loader
     * @param fxmlFile the name of the bundle-private FXML file to load and inflate.
     * @return a Scene object containing the inflated FXML file.
     * @throws IOException thrown if the specified FXML file cannot be loaded.
     */
    Scene loadScene(Class<?> clazz, String fxmlFile) throws IOException;
}
