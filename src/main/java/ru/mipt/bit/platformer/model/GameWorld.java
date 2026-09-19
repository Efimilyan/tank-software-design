package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.List;

public class GameWorld {
    private final Tank tank;
    private final Obstacle obstacle;
    private final GameField gameField;
    private final TileMovement tileMovement;

    public GameWorld(TiledMapTileLayer groundLayer, TextureRegion playerGraphics, TextureRegion treeObstacleGraphics) {
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        tank = new Tank(new GridPoint2(1, 1), playerGraphics);
        obstacle = new Obstacle(new GridPoint2(1, 3), treeObstacleGraphics, groundLayer);
        gameField = new GameField(List.of(obstacle));
    }

    public void update(float deltaTime) {
        tank.updateMovementProgress(deltaTime, tileMovement);
    }

    public Tank getTank() {
        return tank;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public GameField getGameField() {
        return gameField;
    }
}
