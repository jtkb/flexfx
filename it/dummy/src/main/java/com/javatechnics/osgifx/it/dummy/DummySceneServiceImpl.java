package com.javatechnics.osgifx.it.dummy;

import com.javatechnics.osgifx.scene.SceneService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DummySceneServiceImpl implements SceneService
{

    @Override
    public Scene getScene() throws IOException
    {
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        Parent parent;
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            FXMLLoader loader = new FXMLLoader(DummyPanelController.class.getResource("DummyPanel.fxml"));
            parent = loader.load();
            // To get the controller:
            // DummyPanelController dpc = loader.getController();
        }
        finally {
            Thread.currentThread().setContextClassLoader(ccl);
        }
        return new Scene(parent);

    }

    @Override
    public StageStyle getPreferredStageStyle() {
        return StageStyle.DECORATED;
    }
}
