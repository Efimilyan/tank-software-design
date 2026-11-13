package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.api.GridPosition;

public class GridMovement {
    private static final float ARRIVED = 1f;

    private final float speed;
    private GridPosition current;
    private GridPosition destination;
    private float progress = ARRIVED;

    public GridMovement(GridPosition start, float speed) {
        this.current = start;
        this.destination = start;
        this.speed = speed;
    }

    public boolean isIdle() {
        return progress >= ARRIVED;
    }

    public void start(GridPosition target) {
        destination = target;
        progress = 0f;
    }

    public void update(float deltaTime) {
        progress = Math.clamp(progress + deltaTime / speed, 0f, ARRIVED);
        if (isIdle()) {
            current = destination;
        }
    }

    public boolean occupies(GridPosition position) {
        return current.equals(position) || destination.equals(position);
    }

    public GridPosition getCurrent() {
        return current;
    }

    public GridPosition getDestination() {
        return destination;
    }

    public float getProgress() {
        return progress;
    }
}