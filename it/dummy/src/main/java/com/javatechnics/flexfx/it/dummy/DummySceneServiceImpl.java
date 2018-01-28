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

package com.javatechnics.flexfx.it.dummy;

import com.javatechnics.flexfx.platform.Toolkit;
import com.javatechnics.flexfx.scene.SceneService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DummySceneServiceImpl implements SceneService
{
    private Toolkit toolkit;

    private Scene scene = null;

    @Override
    public Scene getScene() throws IOException
    {
        if (scene == null) {
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            Parent parent;
            try {
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                FXMLLoader loader = new FXMLLoader(DummyPanelController.class.getResource("DummyPanel.fxml"));
                parent = loader.load();
                scene = new Scene(parent);
                // To get the controller:
                // DummyPanelController dpc = loader.getController();
            } finally {
                Thread.currentThread().setContextClassLoader(ccl);
            }
        }
        return scene;

    }

    @Override
    public StageStyle getPreferredStageStyle() {
        return StageStyle.UNDECORATED;
    }

    public Toolkit getToolkit()
    {
        return toolkit;
    }

    public void setToolkit(final Toolkit toolkit)
    {
        this.toolkit = toolkit;
    }
}
