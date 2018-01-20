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

package com.javatechnics.flexfx.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Arrays;

/**
 * Bundles should implement this service if they create an instance of a Parent node that is intended to
 * be the root node in the Stage. Implementations should be aware that these methods can be called from a thread
 * outside of the implementing bundle's context hence care must be exercised regarding class/resource visibility.
 */
public interface SceneService
{
    /**
     * Returns the root node to be used within the Stage. This method can be called when the bundle is registered and
     * unregistered. The same object must be returned whenever this method is called.
     *
     * @return the root node.
     * @throws IOException can be thrown if inflating an FXML file as part of the implementation.
     */
    Scene getScene() throws IOException;

    /**
     * The preferred StageStyle for the scene. The preferred style is not guaranteed due to JavaFX toolkit
     * implementation.
     *
     * @return the preferred StageStyle or StageStyle.DECORATED by default.
     */
    default StageStyle getPreferredStageStyle()
    {
        return StageStyle.DECORATED;
    }

    /**
     * Returns the desired screen onto which the Scene is to be placed.
     *
     * @return the preferred Screen onto which the Scene is to be placed.
     * @since 1.1.0
     */
    default Screen getPreferredScreen()
    {
        return Screen.getPrimary();
    }

    /**
     * Indicates if full-screen is desired for this Scene. Full-screen may not be fulfilled dependent upon
     * security implementations.
     *
     * @return true if full-screen is desired.
     * @since 1.1.0
     */
    default boolean getPreferFullScreen()
    {
        return false;
    }

    /**
     * The title of the window for this Scene.
     * @return the title of the window.
     * @since 1.1.0
     */
    default String getTitle()
    {
        return "FlexFX Application";
    }

    /**
     * Images that are to be used for window decoration.
     * Note this method will not be called from the implementing bundles' context. To load resources use the methods of
     * {@link com.javatechnics.flexfx.util.UtilityService}.
     * @return A List of images of various dimensions for the scene.
     * @since 1.1.0
     */
    default ObservableList<Image> getIcons()
    {
        return FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(Arrays.asList(new Image("/colourwheel.png"))));
    }
}
