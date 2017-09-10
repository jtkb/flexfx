package com.javatechnics.osgifx.examples.simple;

import com.javatechnics.osgifx.scene.SceneService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SimpleSceneService implements SceneService
{
    @Override
    public Scene getScene() throws IOException {
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        final Scene scene;
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            scene = new Scene(FXMLLoader.load(SimpleExampleController.class.getResource(SimpleExampleController.SIMPLE_EXAMPLE_FXML_FILE)));
        }
        finally {
            Thread.currentThread().setContextClassLoader(ccl);
        }
        return scene;
    }
}
