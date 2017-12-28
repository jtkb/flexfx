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

package com.javatechnics.flexfx.examples.simple;

import com.javatechnics.flexfx.scene.SceneService;
import com.javatechnics.flexfx.util.Utils;
import javafx.scene.Scene;

import java.io.IOException;

public class SimpleSceneService implements SceneService
{
    private Scene scene;

    /**
     * Loads the bundle-private FXML file and places it inside a Scene object. To deal with the boiler-plate
     * ClassLoader switching this implementation takes advantage of the {@link Utils}
     * SceneLoader functional interface implementation.
     *
     * @return a Scene object populated with inflated FXML file.
     * @throws IOException thrown if the FXML file cannot be found.
     */
    @Override
    public Scene getScene() throws IOException
    {
        if (scene == null)
        {
            scene = Utils.sceneLoader.loadScene(this.getClass(), SimpleExampleController.SIMPLE_EXAMPLE_FXML_FILE);
        }
        return scene;
    }

    /*
    An alternative could be to have a separate initialise method called upon bundle activation. The advantage of this
    is that it would allow the bundle to fail-fast rather than at the point when the service is used as might be the
    case if the specified FXML file cannot be found.

    public void initialise() throws IOException
    {
        scene = Utils.sceneLoader.loadScene(this.getClass(), SimpleExampleController.SIMPLE_EXAMPLE_FXML_FILE);
    }
    */

    //==================================================================================================================

    /*
    What follows below is NOT the recommended way to create a Scene service which has been completed with a Parent node
    loaded from an FXML file. The reason it is not recommended is because it MUST switch class loaders due to the nature
    of OSGi.

    @Override
    public Scene getScene() throws IOException {
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        final Scene scene;
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            scene = new Scene(FXMLLoader.load(SimpleExampleController.class.getResource(SimpleExampleController.SIMPLE_EXAMPLE_FXML_FILE)));
        }
        finally {
            Thread.currentThread().setContextClassLoader(ccl);
        }
        return scene;
    }*/
}
