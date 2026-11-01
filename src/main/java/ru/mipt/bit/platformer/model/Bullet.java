package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Bullet implements GameObject, Movable, Blocking {
    private final Direction direction;
    private final float movementSpeed;
    private final int damage;
    private final CollisionChecker collisionChecker;
    private final GridPoint2 currentCoordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private boolean alive = true;

    public Bullet(GridPoint2 coordinates, Direction direction, float movementSpeed, int damage,
                  CollisionChecker collisionChecker) {
        this.currentCoordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.direction = direction;
        this.movementSpeed = movementSpeed;
        this.damage = damage;
        this.collisionChecker = collisionChecker;
    }

    @Override
    public void update(float deltaTime) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 next = direction.shift(currentCoordinates);
            if (collisionChecker.isOccupied(next)) {
                collisionChecker.hit(next, damage);
                alive = false;
                return;
            }
            destinationCoordinates.set(next);
            movementProgress = 0f;
        }
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (isEqual(movementProgress, 1f)) {
            currentCoordinates.set(destinationCoordinates);
        }
    }

    @Override
    public boolean occupies(GridPoint2 position) {
        return currentCoordinates.equals(position) || destinationCoordinates.equals(position);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public int getDamage() {
        return damage;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public GridPoint2 getCurrentCoordinates() {
        return currentCoordinates;
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return direction.rotation();
    }
}
