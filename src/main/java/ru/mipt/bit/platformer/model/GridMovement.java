package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class GridMovement {
    private static final float ARRIVED = 1f;

    private final float speed;
    private final GridPoint2 current;
    private final GridPoint2 destination;
    private float progress = ARRIVED;

    public GridMovement(GridPoint2 start, float speed) {
        this.current = new GridPoint2(start);
        this.destination = new GridPoint2(start);
        this.speed = speed;
    }

    public boolean isIdle() {
        return isEqual(progress, ARRIVED);
    }

    public void start(GridPoint2 target) {
        destination.set(target);
        progress = 0f;
    }

    public void update(float deltaTime) {
        progress = clamp(progress + deltaTime / speed, 0f, ARRIVED);
        if (isIdle()) {
            current.set(destination);
        }
    }

    public boolean occupies(GridPoint2 position) {
        return current.equals(position) || destination.equals(position);
    }

    public GridPoint2 getCurrent() {
        return current;
    }

    public GridPoint2 getDestination() {
        return destination;
    }

    public float getProgress() {
        return progress;
    }
}