package ru.mipt.bit.platformer.model;

@FunctionalInterface
public interface Spawner {
    void spawn(GameObject object);
}