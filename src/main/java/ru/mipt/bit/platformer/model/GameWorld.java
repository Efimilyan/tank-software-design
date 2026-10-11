package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    private final Tank tank;
    private final List<Obstacle> obstacles;
    private final List<GameObject> gameObjects = new ArrayList<>();
    private final GameField gameField;

    public GameWorld(Tank tank, List<Obstacle> obstacles) {
        this.tank = tank;
        this.obstacles = obstacles;
        this.gameField = new GameField(obstacles);
        gameObjects.add(tank);
        gameObjects.addAll(obstacles);
    }

    public void update(float deltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }
    }

    public Tank getTank() {
        return tank;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public GameField getGameField() {
        return gameField;
    }
}