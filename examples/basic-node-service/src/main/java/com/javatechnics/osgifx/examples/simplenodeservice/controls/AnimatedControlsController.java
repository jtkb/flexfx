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

package com.javatechnics.osgifx.examples.simplenodeservice.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;

/**
 * The controller class for the AnimatedControls FXML layout.
 */
public class AnimatedControlsController
{
    public static final String FXML_FILE = "AnimatedControls.fxml";
    private ControlsCallback controlsCallback;
    @FXML
    private Slider speedSlider;
    @FXML
    private ToggleGroup effectToggleGroup;
    @FXML
    private RadioButton blur;
    @FXML
    private RadioButton bloom;
    @FXML
    private RadioButton glow;

    public void initialize()
    {
        blur.setOnAction(new EffectEventHandler(ControlsCallback.EffectType.BLUR));
        bloom.setOnAction(new EffectEventHandler(ControlsCallback.EffectType.BLOOM));
        glow.setOnAction(new EffectEventHandler(ControlsCallback.EffectType.GLOW));
    }

    void setControlsCallback(final ControlsCallback controlsCallback)
    {
        this.controlsCallback = controlsCallback;
    }

    /**
     * Private class to handle radio button events. An instance is created for each of the
     * effect radio buttons.
     */
    private class EffectEventHandler implements EventHandler<ActionEvent>
    {
        ControlsCallback.EffectType effect;

        public EffectEventHandler(final ControlsCallback.EffectType effect)
        {
            this.effect = effect;
        }

        @Override
        public void handle(final ActionEvent event)
        {
            controlsCallback.event(ControlsCallback.NodeEvent.BLEND, effect);
        }
    }
}
