package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.api.*;

public class Tank implements GameObject, Blocking, Movable {
    public static final int DEFAULT_HEALTH = 3;

    private final Gun gun;
    private final GridMovement movement;
    private Direction direction = Direction.RIGHT;
    private int health;

    public Tank(GridPoint2 coordinates, float movementSpeed) {
        this(coordinates, movementSpeed, DEFAULT_HEALTH);
    }

    public Tank(GridPoint2 coordinates, float movementSpeed, int health) {
        this(coordinates, movementSpeed, health, Gun.NONE);
    }

    public Tank(GridPoint2 coordinates, float movementSpeed, int health, Gun gun) {
        this.gun = gun;
        this.movement = new GridMovement(coordinates, movementSpeed);
        this.health = health;
    }

    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (movement.isIdle()) {
            GridPoint2 nextCoordinates = direction.shift(movement.getCurrent());
            if (!collisionChecker.isOccupied(nextCoordinates)) {
                movement.start(nextCoordinates);
            }
            this.direction = direction;
        }
    }
    public void shoot() {
        gun.fire(direction.shift(movement.getDestination()), direction);
    }

    @Override
    public void update(float deltaTime) {
        gun.update(deltaTime);
        movement.update(deltaTime);
    }

    @Override
    public boolean occupies(GridPoint2 position) {
        return movement.occupies(position);
    }

    @Override
    public GridPoint2 getCurrentCoordinates() {
        return movement.getCurrent();
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return movement.getDestination();
    }

    @Override
    public float getMovementProgress() {
        return movement.getProgress();
    }

    @Override
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

    @Override
    public void hit(int damage) {
        takeDamage(damage);
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }
}