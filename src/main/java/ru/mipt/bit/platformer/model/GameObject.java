package ru.mipt.bit.platformer.model;

public interface GameObject {
    void update(float deltaTime);

    default boolean isAlive() {
        return true;
    }
}
