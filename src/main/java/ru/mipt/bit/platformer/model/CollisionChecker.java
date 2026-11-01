package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface CollisionChecker {
    boolean isOccupied(GridPoint2 coordinates);

    default void hit(GridPoint2 coordinates, int damage) {
    }
}
