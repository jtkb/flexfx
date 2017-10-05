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
 *
 * @param <T> the controller type.
 */
public final class ControllerWrapper<T>
{
    public static final String CONTROLLER_FIELD_NAME = "controller";

    public static final String NODE_FIELD_NAME = "parent";

    private T controller;

    private Class<T> controllerClass = null;

    private Parent parent;

    public ControllerWrapper(final Class<T> controllerClass)
    {
        this.controllerClass = controllerClass;
    }

    public final T getController()
    {
        return controller;
    }

    public final Parent getParent()
    {
        return parent;
    }

    public Class<T> getControllerClass()
    {
        return controllerClass;
    }
}
