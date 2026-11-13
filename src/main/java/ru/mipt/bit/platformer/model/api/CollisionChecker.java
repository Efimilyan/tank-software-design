package ru.mipt.bit.platformer.model.api;

public interface CollisionChecker {
    boolean isOccupied(GridPosition coordinates);
}