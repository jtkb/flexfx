package com.javatechnics.osgifx.scene;

import javafx.scene.Scene;
import javafx.stage.StageStyle;

public interface SceneService
{
    Scene getScene();

    default StageStyle getPreferredStageStyle() { return StageStyle.DECORATED; }
}
