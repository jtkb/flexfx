package com.javatechnics.flexfx.examples.ds.dummy.fxml;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class DSController
{
    public static final String FXML_FILE = "DS.fxml";

    @FXML
    private Button buttonOne;

    @FXML
    private Button buttonTwo;

    @FXML
    private Button buttonThree;

    private TextArea textArea;

    public void initialize()
    {
        buttonOne.setOnAction(event -> textArea.appendText("hello"));
    }
}
