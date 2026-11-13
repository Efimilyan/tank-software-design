package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayDeque;
import java.util.Deque;

public class OwnedResources implements Disposable {
    private final Deque<Disposable> resources = new ArrayDeque<>();

    public <T extends Disposable> T own(T resource) {
        resources.push(resource);
        return resource;
    }

    @Override
    public void dispose() {
        while (!resources.isEmpty()) {
            resources.pop().dispose();
        }
    }
}