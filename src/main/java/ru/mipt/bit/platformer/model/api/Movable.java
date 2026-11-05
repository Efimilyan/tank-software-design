package ru.mipt.bit.platformer.model.api;

import com.badlogic.gdx.math.GridPoint2;

public interface Movable {
    GridPoint2 getCurrentCoordinates();

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    float getRotation();
}
