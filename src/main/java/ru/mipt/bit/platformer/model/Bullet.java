package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.api.*;

public class Bullet implements GameObject, Movable, Blocking {
    private final Direction direction;
    private final int damage;
    private final BattleField battleField;
    private final GridMovement movement;
    private boolean alive = true;

    public Bullet(GridPoint2 coordinates, Direction direction, float movementSpeed, int damage,
                  BattleField battleField) {
        this.movement = new GridMovement(coordinates, movementSpeed);
        this.direction = direction;
        this.damage = damage;
        this.battleField = battleField;
    }

    @Override
    public void update(float deltaTime) {
        if (movement.isIdle()) {
            GridPoint2 next = direction.shift(movement.getCurrent());
            if (battleField.isOccupied(next)) {
                battleField.hit(next, damage);
                alive = false;
                return;
            }
            movement.start(next);
        }
        movement.update(deltaTime);
    }

    @Override
    public boolean occupies(GridPoint2 position) {
        return movement.occupies(position);
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
}