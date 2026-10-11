package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameWorldTest {
    private static final float TANK_SPEED = 0.4f;

    private final TextureRegion region = new TextureRegion();
    private final TiledMapTileLayer layer = new TiledMapTileLayer(10, 8, 128, 128);
    private final GameWorld world = new GameWorld(
            new Tank(new GridPoint2(1, 1), region, TANK_SPEED),
            List.of(new Obstacle(new GridPoint2(1, 3))),
            new TileMovement(layer, Interpolation.smooth));

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