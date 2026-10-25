package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank implements GameObject{

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
            GridPoint2 nextCoordinates = direction.shift(currentCoordinates);
            if (!collisionChecker.isOccupied(nextCoordinates)) {
                destinationCoordinates.set(nextCoordinates);
                movementProgress = 0f;
            }
            rotation = direction.rotation();
        }
    }
    @Override
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            currentCoordinates.set(destinationCoordinates);
        }
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