package com.javatechnics.osgifx.stage.test;

import javafx.event.EventDispatchChain;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class is intended for test purposes only and wraps the real Stage object.
 */
public final class TestStage extends Stage
{
    private Stage trueStage;

    public TestStage(final Stage trueStage) {
        this.trueStage = trueStage;
    }

    public TestStage(final StageStyle style, final Stage trueStage) {
        super(style);
        this.trueStage = trueStage;
    }

    public TestStage() {
        super();
    }

    public TestStage(final StageStyle style) {
        super(style);
    }

    @Override
    public void showAndWait() {
        trueStage.showAndWait();
    }

    @Override
    public void toFront() {
        trueStage.toFront();
    }

    @Override
    public void toBack() {
        trueStage.toBack();
    }

    @Override
    public void close() {
        trueStage.close();
    }

    @Override
    public void sizeToScene() {
        trueStage.sizeToScene();
    }

    @Override
    public void centerOnScreen() {
        trueStage.centerOnScreen();
    }

    @Override
    public boolean hasProperties() {
        return trueStage.hasProperties();
    }

    @Override
    public void setUserData(final Object value) {
        trueStage.setUserData(value);
    }

    @Override
    public Object getUserData() {
        return trueStage.getUserData();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(final EventDispatchChain tail) {
        return super.buildEventDispatchChain(tail);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
