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

/**
 * Immutable class that contains the parent node specified in an FXML file and its associated controller.
 * @param <T> the controller type.
 */
public final class  ControllerWrapper <T>
{
    final T controller;

    final Parent parent;

    public ControllerWrapper(final T controller, final Parent parent)
    {
        this.controller = controller;
        this.parent = parent;
    }

    public T getController()
    {
        return controller;
    }

    public Parent getParent()
    {
        return parent;
    }
}
