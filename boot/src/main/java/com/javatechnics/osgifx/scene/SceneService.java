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
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Bundles should implement this service if they create an instance of a Parent node that is intended to
 * be the root node in the Stage.
 */
public interface SceneService
{
    /**
     * Returns the root node to be used within the Stage.
     * @return the root node.
     * @throws IOException can be thrown if inflating an FXML file as part of the implementation.
     */
    Scene getScene() throws IOException;

    /**
     * The preferred StageStyle for the scene. The preferred style is not guaranteed due to JavaFX toolkit
     * implementation.
     * @return the preferred StageStyle or StageStyle.DECORATED by default.
     */
    default StageStyle getPreferredStageStyle() { return StageStyle.DECORATED; }
}
