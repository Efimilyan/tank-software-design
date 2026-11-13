package ru.mipt.bit.platformer.model.api;

public interface Movable {
    GridPosition getCurrentCoordinates();

    GridPosition getDestinationCoordinates();

    float getMovementProgress();

    float getRotation();
}
