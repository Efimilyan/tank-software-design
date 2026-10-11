package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameWorldTest {
    private static final float TANK_SPEED = 0.4f;

    private final GameWorld world = new GameWorld(
            new Tank(new GridPoint2(1, 1), TANK_SPEED),
            List.of(new Obstacle(new GridPoint2(1, 3))));

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

        assertEquals(new GridPoint2(1, 2), tank.getCurrentCoordinates());
    }

    @Test
    void tankCannotDriveThroughObstacle() {
        Tank tank = world.getTank();
        tank.move(Tank.Direction.UP, world.getGameField());
        world.update(0.4f);
        tank.move(Tank.Direction.UP, world.getGameField());

        world.update(0.4f);
        world.update(0.1f);

        assertEquals(new GridPoint2(1, 2), tank.getCurrentCoordinates());
    }
}