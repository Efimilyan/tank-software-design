package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.util.TileMovement;

import java.util.List;

public class GameWorld {
    private final Tank tank;
    private final List<Obstacle> obstacles;
    private final GameField gameField;
    private final TileMovement tileMovement;

    public GameWorld(Tank tank, List<Obstacle> obstacles, TileMovement tileMovement) {
        this.tank = tank;
        this.obstacles = obstacles;
        this.tileMovement = tileMovement;
        this.gameField = new GameField(obstacles);
    }

    public void update(float deltaTime) {
        tank.updateMovementProgress(deltaTime, tileMovement);
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