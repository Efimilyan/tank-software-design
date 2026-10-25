package ru.mipt.bit.platformer.model;

import java.util.List;

public class GameWorld {
    private final List<GameObject> gameObjects;

    public GameWorld(List<? extends GameObject> gameObjects) {
        this.gameObjects = List.copyOf(gameObjects);
    }

    public void update(float deltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }
    }
}