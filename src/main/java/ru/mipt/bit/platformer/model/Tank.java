package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank implements GameObject, Blocking {
    public static final int DEFAULT_HEALTH = 3;

    private final float movementSpeed;
    private final GridPoint2 currentCoordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;
    private int health;

    public Tank(GridPoint2 coordinates, float movementSpeed) {
        this(coordinates, movementSpeed, DEFAULT_HEALTH);
    }

    public Tank(GridPoint2 coordinates, float movementSpeed, int health) {
        this.currentCoordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.movementSpeed = movementSpeed;
        this.health = health;
    }

    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 nextCoordinates = direction.shift(currentCoordinates);
            if (!collisionChecker.isOccupied(nextCoordinates)) {
                destinationCoordinates.set(nextCoordinates);
                movementProgress = 0f;
            }
            this.direction = direction;
        }
    }
    @Override
    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            currentCoordinates.set(destinationCoordinates);
        }
    }

    @Override
    public boolean occupies(GridPoint2 position) {
        return currentCoordinates.equals(position) || destinationCoordinates.equals(position);
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
        return direction.rotation();
    }

    public Direction getDirection() {
        return direction;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Урон не может быть отрицательным: " + damage);
        }
        health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }
}