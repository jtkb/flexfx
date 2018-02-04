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
    private volatile double velocityX = 220.0 / 1E9;
    private volatile double velocityY = 220.0 / 1E9;
    private final Object LOCK_OBJECT = new Object();


    public ShapeAnimator(final Circle circle)
    {
        this.circle = circle;
        this.parent = circle.getParent();
        this.circleRadius = this.circle.getRadius();
    }

    @Override
    public void handle(final long now)
    {
        synchronized (LOCK_OBJECT)
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

                if ((circleBounds.getMinX() <= parentBounds.getMinX() ) && velocityX < 0.0 || (circleBounds.getMaxX() >= parentBounds.getMaxX() && velocityX > 0.0))
                {
                    velocityX *= -1.0;
                }

                if ((circleBounds.getMinY() <= parentBounds.getMinY() && velocityY < 0.0) || (circleBounds.getMaxY() >= parentBounds.getMaxY() && velocityY > 0.0))
                {
                    velocityY *= -1.0;
                }
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

    public void setVelocity(final double velocity)
    {
        synchronized (LOCK_OBJECT)
        {
            velocityX = velocityX < 0 ? -1.0 * velocity / 1E9 : velocity / 1E9;
            velocityY = velocityY < 0 ? -1.0 * velocity / 1E9 : velocity / 1E9;
        }
    }
}
