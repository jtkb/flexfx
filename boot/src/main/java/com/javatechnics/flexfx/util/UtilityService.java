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
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.List;

/**
 * OSGi service that offers a safe way to use JavaFx methods that rely on the initialisation of the JavaFx
 * Toolkit. Service implementations should not be available until the toolkit has been initialised.
 */
public interface UtilityService
{
    /**
     * Populates the specified wrapper with the controller specified within the FXML file.
     * @param controllerWrapper the wrapper object to populate.
     * @param fxmlFile the FXML file name to inflate.
     * @throws IOException thrown if the ParentNode cannot be loaded.
     * @throws ClassCastException thrown if the FXML controller cannot be cast to the specified controller class.
     */
    void populateWrapper(ControllerWrapper controllerWrapper, String fxmlFile) throws IOException;

    /**
     * Loads images that may be private to a bundle and handles switching the thread context classloader.
     * @param imageLocations a List specifying the image locations within the bundle.
     * @param clazz a Class within the bundle containing the images.
     * @return a populated list on Images or empty if none are found.
     */
    List<Image> loadBundleImages(List<String> imageLocations, Class<?> clazz);
}
