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

package com.javatechnics.flexfx.examples.simplenodeservice.controls;

import com.javatechnics.flexfx.node.ControllerWrapper;
import com.javatechnics.flexfx.node.NodeService;
import com.javatechnics.flexfx.util.UtilityService;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * //TODO: Improve this description.
 * This class is the OSGi adapter class for the FXML controller of the Controls layout.
 */
public class AnimatedNodeControls implements NodeService, ControlsCallback
{
    private static final String FXML_FILE = "AnimatedControls.fxml";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private AnchorPane parentNode = null;

    private AnimatedControlsController animatedNodeController = null;

    private EventAdmin eventAdmin = null;

    private UtilityService utilityService = null;

    private ControlsCallback controlsCallback = (NodeEvent nodeEvent, Object value) ->
    {
        executorService.execute(() ->
        {
            Event event = null;
            switch (nodeEvent)
            {
                case BLEND:
                    final Map<String, ControlsCallback.EffectType> map = new HashMap<>(1);
                    map.put("blend", (EffectType) value);
                    event = new Event(ControlsCallback.BLEND_SUB_TOPIC, map);
                    break;
                case SPEED:
                    final Map<String, Double> speedMap = new HashMap<>(1);
                    speedMap.put("speed", (Double)value);
                    event = new Event(ControlsCallback.SPEED_SUB_TOPIC, speedMap);
                    break;
            }
            eventAdmin.sendEvent(event);
        });
    };

    // TODO: create an implementation of the functional interface ControlsCallback and pass into the FXML controller
    // class. The implementation will make use of the EventAdmin property above.

    @Override
    public Parent getParentNode()
    {
        return parentNode;
    }

    @Override
    public void event(final NodeEvent nodeEvent, final Object value)
    {

    }

    public void initialise() throws IOException, NoSuchFieldException, IllegalAccessException
    {
        ControllerWrapper<AnimatedControlsController> wrapper = new ControllerWrapper<>(AnimatedControlsController.class);
        utilityService.populateWrapper(wrapper, AnimatedNodeControls.FXML_FILE);
        parentNode = (AnchorPane) wrapper.getParent();
        animatedNodeController = wrapper.getController();
        animatedNodeController.setControlsCallback(controlsCallback);
    }

    public void destroy()
    {

    }

    public void setEventAdmin(final EventAdmin eventAdmin)
    {
        this.eventAdmin = eventAdmin;
    }

    public void setUtilityService(final UtilityService utilityService)
    {
        this.utilityService = utilityService;
    }
}
