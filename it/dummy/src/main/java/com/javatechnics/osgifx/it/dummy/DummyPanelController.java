package com.javatechnics.osgifx.it.dummy;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

public class DummyPanelController
{

    @FXML
    Circle circle1;

    @FXML
    Circle circle2;

    @FXML
    Circle circle3;

    @FXML
    Button button;

    public DummyPanelController() {
    }

    public Parent getParent()
    {
        return null;
    }
}
