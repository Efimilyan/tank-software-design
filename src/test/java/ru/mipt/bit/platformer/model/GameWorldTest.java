package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameWorldTest {
    private final GameWorld world = new GameWorld(
            new TiledMapTileLayer(10, 8, 128, 128),
            new TextureRegion(),
            new TextureRegion());

    @Test
    void obstacleTileIsOccupiedInGameField() {
        assertTrue(world.getGameField().isOccupied(new GridPoint2(1, 3)));
    }

    @Test
    void updateMovesTank() {
        Tank tank = world.getTank();
        tank.move(Tank.Direction.UP, world.getGameField());

        world.update(0.4f);
        world.update(0.1f);

        assertEquals(192f, tank.getBoundingRectangle().x, 0.001f);
        assertEquals(320f, tank.getBoundingRectangle().y, 0.001f);
    }

    @Test
    void tankCannotDriveThroughObstacle() {
        Tank tank = world.getTank();
        tank.move(Tank.Direction.UP, world.getGameField());
        world.update(0.4f);
        tank.move(Tank.Direction.UP, world.getGameField());

        world.update(0.4f);
        world.update(0.1f);

        assertEquals(192f, tank.getBoundingRectangle().x, 0.001f);
        assertEquals(320f, tank.getBoundingRectangle().y, 0.001f);
    }
}