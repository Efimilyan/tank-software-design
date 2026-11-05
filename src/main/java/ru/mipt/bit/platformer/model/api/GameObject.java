package ru.mipt.bit.platformer.model.api;

public interface GameObject {
    void update(float deltaTime);

    default boolean isAlive() {
        return true;
    }
}
