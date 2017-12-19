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

import com.javatechnics.flexfx.node.ControllerWrapper;
import com.javatechnics.flexfx.node.NodeService;
import com.javatechnics.flexfx.scene.SceneService;
import com.javatechnics.flexfx.util.UtilityService;
import javafx.scene.Scene;

import java.io.IOException;

public class AnimatedNodeSceneService<T> implements SceneService
{
    private Scene scene = null;

    private SceneServiceController sceneServiceController = null;

    private UtilityService utilityService = null;

    @Override
    public Scene getScene() throws IOException
    {
        return scene;
    }

    /**
     * Called by Blueprint manager.
     */
    public void initialise() throws IOException, NoSuchFieldException, IllegalAccessException
    {
        ControllerWrapper<SceneServiceController> wrapper = new ControllerWrapper<SceneServiceController>(SceneServiceController.class);
        utilityService.populateWrapper(wrapper, SceneServiceController.FXML_FILE);
        sceneServiceController = wrapper.getController();
        scene = new Scene(wrapper.getParent());
    }

    public final void bindNodeService(final NodeService nodeService)
    {
        sceneServiceController.bindParentNode(nodeService.getParentNode());
    }

    public final void unbindNodeService(final NodeService nodeService)
    {
        if (nodeService != null)
        {
            sceneServiceController.unbindParentNode(nodeService.getParentNode());
        }
    }

    public void setUtilityService(final UtilityService utilityService)
    {
        this.utilityService = utilityService;
    }
}
