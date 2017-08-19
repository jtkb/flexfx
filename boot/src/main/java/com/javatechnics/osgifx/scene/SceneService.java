package com.javatechnics.osgifx.scene;

import javafx.scene.Scene;
import javafx.stage.StageStyle;

import java.io.IOException;

public interface SceneService
{
    Scene getScene() throws IOException;

    default StageStyle getPreferredStageStyle() { return StageStyle.DECORATED; }
}
