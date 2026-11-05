package ru.mipt.bit.platformer.model.api;

import com.badlogic.gdx.math.GridPoint2;

public interface CollisionChecker {
    boolean isOccupied(GridPoint2 coordinates);
}