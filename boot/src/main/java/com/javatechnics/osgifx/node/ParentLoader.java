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

package com.javatechnics.osgifx.node;

import javafx.scene.Parent;

import java.io.IOException;

public interface ParentLoader
{
    /**
     * The purpose of this method is to load a specified FXML file and inflate it returning the top-level Parent object.
     * Imlementations should handle the switching back and forth of the appropriate Class Loader.
     * @param clazz a Class object that is in the same bundle as the FXML file.
     * @param fxmlFile the specified FXML file to load and inflate.
     * @return the top-level Parent object of the inflated FXML file.
     * @throws IOException thrown if the specified FXML file cannot be found.
     */
    Parent loadParent(Class<?> clazz, String fxmlFile) throws IOException;
}
