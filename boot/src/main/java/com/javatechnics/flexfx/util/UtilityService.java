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

import java.io.IOException;

/**
 * OSGi service that offers a safe way to use JavaFx methods that rely on the initialisation of the JavaFx
 * Toolkit. Service implementations should not be available until the toolkit has been initialised.
 */
public interface UtilityService
{
    /**
     * Populates the specified wrapper with the controller specified within the FXML file.
     * @param utilityControllerWrapper the wrapper object to populate.
     * @param fxmlFile the FXML file name to inflate.
     * @throws IOException
     */
    void populateWrapper(ControllerWrapper utilityControllerWrapper, String fxmlFile) throws IOException, NoSuchFieldException, IllegalAccessException;
}
