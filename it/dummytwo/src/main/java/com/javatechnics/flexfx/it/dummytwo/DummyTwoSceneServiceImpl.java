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

package com.javatechnics.flexfx.it.dummytwo;

import com.javatechnics.flexfx.scene.SceneService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class DummyTwoSceneServiceImpl implements SceneService {

    @Override
    public Scene getScene() throws IOException {

        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        Parent parent;
        try
        {
            parent = FXMLLoader.load(this.getClass().getResource("DummyTwoPanel.fxml"));
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(ccl);
        }

        return new Scene(parent);
    }

}
