package com.javatechnics.osgifx.it.dummy;

import com.javatechnics.osgifx.scene.SceneService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DummySceneServiceImpl implements SceneService
{
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
}
