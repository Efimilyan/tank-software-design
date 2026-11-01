package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Level implements GameObject {
    private final Set<GameObject> objects = new LinkedHashSet<>();
    private final List<LevelListener> listeners = new ArrayList<>();

    public void subscribe(LevelListener listener) {
        listeners.add(listener);
    }

    public void add(GameObject object) {
        if (objects.add(object)) {
            for (LevelListener listener : List.copyOf(listeners)) {
                listener.objectAdded(object);
            }
        }
    }

    public void remove(GameObject object) {
        if (objects.remove(object)) {
            for (LevelListener listener : List.copyOf(listeners)) {
                listener.objectRemoved(object);
            }
        }
    }

    public List<GameObject> objects() {
        return List.copyOf(objects);
    }

    @Override
    public void update(float deltaTime) {
        for (GameObject object : List.copyOf(objects)) {
            if (objects.contains(object) && object.isAlive()) {
                object.update(deltaTime);
            }
        }
        removeDeadObjects();
    }

    private void removeDeadObjects() {
        for (GameObject object : List.copyOf(objects)) {
            if (!object.isAlive()) {
                remove(object);
            }
        }
    }
}
