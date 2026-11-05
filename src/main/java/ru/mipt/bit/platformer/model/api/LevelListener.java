package ru.mipt.bit.platformer.model.api;

public interface LevelListener {
    void objectAdded(GameObject object);

    void objectRemoved(GameObject object);
}
