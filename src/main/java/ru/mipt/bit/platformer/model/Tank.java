package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank implements GameObject{

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    private final float movementSpeed;
    private final GridPoint2 currentCoordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(GridPoint2 coordinates, float movementSpeed) {
        this.currentCoordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.movementSpeed = movementSpeed;
    }

    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 nextCoordinates = nextCoordinatesFor(direction);
            if (!collisionChecker.isOccupied(nextCoordinates)) {
                destinationCoordinates.set(nextCoordinates);
                movementProgress = 0f;
            }
            rotation = rotationFor(direction);
        }
    }
    @Override
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            currentCoordinates.set(destinationCoordinates);
        }
    }

    private GridPoint2 nextCoordinatesFor(Direction direction) {
        return switch (direction) {
            case UP -> incrementedY(currentCoordinates);
            case DOWN -> decrementedY(currentCoordinates);
            case LEFT -> decrementedX(currentCoordinates);
            case RIGHT -> incrementedX(currentCoordinates);
        };
    }

    private float rotationFor(Direction direction) {
        return switch (direction) {
            case UP -> 90f;
            case DOWN -> -90f;
            case LEFT -> -180f;
            case RIGHT -> 0f;
        };
    }

    public GridPoint2 getCurrentCoordinates() {
        return currentCoordinates;
    }

    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return rotation;
    }
}