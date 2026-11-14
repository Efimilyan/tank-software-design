package ru.mipt.bit.platformer.model.api;

/** Порт: события появления и удаления объектов. */
public interface LevelListener {
    void objectAdded(GameObject object);

    void objectRemoved(GameObject object);
}
