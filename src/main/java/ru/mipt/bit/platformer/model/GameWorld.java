package ru.mipt.bit.platformer.model;

import java.util.List;

public class GameWorld {
    private final Tank tank;
    private final List<Obstacle> obstacles;
    private final GameField gameField;

    public GameWorld(Tank tank, List<Obstacle> obstacles) {
        this.tank = tank;
        this.obstacles = obstacles;
        this.gameField = new GameField(obstacles);
    }

    public void update(float deltaTime) {
        tank.update(deltaTime);
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