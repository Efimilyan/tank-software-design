package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.api.*;

public class Tank implements GameObject, Blocking, Movable, HasHealth {
    public static final int DEFAULT_HEALTH = 3;

    private final Gun gun;
    private final GridMovement movement;
    private Direction direction = Direction.RIGHT;
    private final Health health;

    public Tank(GridPosition coordinates, float movementSpeed) {
        this(coordinates, movementSpeed, DEFAULT_HEALTH);
    }

    public Tank(GridPosition coordinates, float movementSpeed, int health) {
        this(coordinates, movementSpeed, health, Gun.NONE);
    }

    public Tank(GridPosition coordinates, float movementSpeed, int health, Gun gun) {
        this.gun = gun;
        this.movement = new GridMovement(coordinates, movementSpeed);
        this.health = new Health(health);

    }

    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (movement.isIdle()) {
            GridPosition nextCoordinates = direction.shift(movement.getCurrent());
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
    public boolean occupies(GridPosition position) {
        return movement.occupies(position);
    }

    @Override
    public GridPosition getCurrentCoordinates() {
        return movement.getCurrent();
    }

    @Override
    public GridPosition getDestinationCoordinates() {
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

    @Override
    public int getHealth() {
        return health.getCurrent();
    }
    @Override
    public int getMaxHealth() {
        return health.getMax();
    }

    public void takeDamage(int damage) {
        health.takeDamage(damage);
    }

    @Override
    public void hit(int damage) {
        takeDamage(damage);
    }

    @Override
    public boolean isAlive() {
        return health.isAlive();
    }
}