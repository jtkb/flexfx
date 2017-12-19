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

package com.javatechnics.flexfx.examples.simplenodeservice.node;

import com.javatechnics.flexfx.examples.simplenodeservice.controls.ControlsCallback;
import com.javatechnics.flexfx.node.ControllerWrapper;
import com.javatechnics.flexfx.node.NodeService;
import com.javatechnics.flexfx.util.UtilityService;
import javafx.scene.Parent;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import java.io.IOException;

public class AnimatedNode implements NodeService, EventHandler
{
    private Parent parentNode = null;
    private AnimatedNodeController controller;
    private UtilityService utilityService = null;

    @Override
    public void handleEvent(final Event event)
    {
        switch (event.getTopic())
        {
            case ControlsCallback.SPEED_SUB_TOPIC:
                controller.setSpeed(Integer.getInteger(event.getProperty("speed").toString()));
                break;
            case ControlsCallback.BLEND_SUB_TOPIC:
                controller.setEffect(ControlsCallback.EffectType.valueOf(event.getProperty("blend").toString()));
                break;
        }
    }

    @Override
    public Parent getParentNode()
    {
        return parentNode;
    }

    public void initialise() throws IOException, NoSuchFieldException, IllegalAccessException
    {
        ControllerWrapper<AnimatedNodeController> wrapper = new ControllerWrapper<>(AnimatedNodeController.class);
        utilityService.populateWrapper(wrapper, AnimatedNodeController.FXML_FILE);
        parentNode = wrapper.getParent();
        controller = wrapper.getController();
    }

    public void setUtilityService(final UtilityService utilityService)
    {
        this.utilityService = utilityService;
    }
}
