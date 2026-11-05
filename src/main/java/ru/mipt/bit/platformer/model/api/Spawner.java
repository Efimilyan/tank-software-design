package ru.mipt.bit.platformer.model.api;

@FunctionalInterface
public interface Spawner {
    void spawn(GameObject object);
}