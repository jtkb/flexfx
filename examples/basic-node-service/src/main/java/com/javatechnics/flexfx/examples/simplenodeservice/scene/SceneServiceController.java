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

package com.javatechnics.flexfx.examples.simplenodeservice.scene;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

public class SceneServiceController
{
    public static final String FXML_FILE = "SceneService.fxml";

    private static final int MAX_CHILD_COUNT = 2;

    @FXML
    private GridPane gridPane;

    public void initialize()
    {
        // TODO: Get the number of allowable children in the gridPane - row * column.
    }

    /**
     * Method to add a top-level Node to the Scene. The node will only be added if the number of children is less
     * than the maximum allowable i.e. row * column.
     * @param parentNode the Node to be added to the scene.
     */
    void bindParentNode(final Parent parentNode)
    {
        Platform.runLater(() ->
        {
            if (gridPane.getChildren().size() < MAX_CHILD_COUNT)
            {
                gridPane.getChildren().add(parentNode);
            }
        });
    }

    /**
     * Remove a Node from the Scene.
     * @param parentNode the specified Node to remove.
     */
    void unbindParentNode(final Parent parentNode)
    {
        Platform.runLater(() ->
        {
            if (gridPane.getChildren().contains(parentNode))
            {
                gridPane.getChildren().remove(parentNode);
            }
        });
    }
}
