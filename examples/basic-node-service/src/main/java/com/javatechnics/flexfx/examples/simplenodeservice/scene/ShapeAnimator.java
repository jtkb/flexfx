package com.javatechnics.flexfx.examples.simplenodeservice.scene;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.shape.Circle;

public class ShapeAnimator extends AnimationTimer
{
    private Circle circle;
    private Parent parent;
    private double circleRadius;
    private long previousTime;
    private double velocityX = 220.0 / 1E9;
    private double velocityY = 220.0 / 1E9;


    public ShapeAnimator(final Circle circle)
    {
        this.circle = circle;
        this.parent = circle.getParent();
        this.circleRadius = this.circle.getRadius();
    }

    @Override
    public void handle(final long now)
    {
        final double newX = circle.getCenterX() + (velocityX * (now - previousTime));
        final double newY = circle.getCenterY() + (velocityY * (now - previousTime));
        circle.setCenterX(circle.getCenterX() + (velocityX * (now - previousTime)));
        circle.setCenterY(circle.getCenterY() + (velocityY * (now - previousTime)));

        final Bounds parentBounds = parent.getLayoutBounds();
        final Bounds circleBounds = circle.getBoundsInParent();

        final boolean contains = parentBounds.contains(circleBounds);
        if (!contains)
        {

            if (circleBounds.getMinX() <= parentBounds.getMinX() || circleBounds.getMaxX() >= parentBounds.getMaxX())
            {
                velocityX *= -1.0;
            }

            if (circleBounds.getMinY() <= parentBounds.getMinY() || circleBounds.getMaxY() >= parentBounds.getMaxY())
            {
                velocityY *= -1.0;
            }
        }
        previousTime = now;
    }

    @Override
    public void start()
    {
        super.start();
        previousTime = System.nanoTime();
    }
}
