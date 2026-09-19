package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {
    private final TextureRegion region = new TextureRegion();
    private final TiledMapTileLayer layer = new TiledMapTileLayer(10, 8, 128, 128);
    private final TileMovement tileMovement = new TileMovement(layer, Interpolation.linear);
    private final GameField emptyField = new GameField(List.of());

    @Test
    void newTankFacesRight() {
        Tank tank = new Tank(new GridPoint2(1, 1), region);

        assertEquals(0f, tank.getRotation(), 0.001f);
    }

    @Test
    void moveTurnsTankInThatDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), region);
        tank.move(Tank.Direction.UP, emptyField);

        assertEquals(90f, tank.getRotation(), 0.001f);
    }

    @Test
    void tankMovesToNextTile() {
        Tank tank = new Tank(new GridPoint2(1, 1), region);
        tank.move(Tank.Direction.UP, emptyField);

        tank.updateMovementProgress(0.4f, tileMovement);
        tank.updateMovementProgress(0.1f, tileMovement);

        assertEquals(192f, tank.getBoundingRectangle().x, 0.001f);
        assertEquals(320f, tank.getBoundingRectangle().y, 0.001f);
    }

    @Test
    void tankDoesNotEnterObstacle() {
        Obstacle obstacle = new Obstacle(new GridPoint2(1, 2), region, layer);
        GameField field = new GameField(List.of(obstacle));
        Tank tank = new Tank(new GridPoint2(1, 1), region);

        tank.move(Tank.Direction.UP, field);
        tank.updateMovementProgress(0.4f, tileMovement);
        tank.updateMovementProgress(0.1f, tileMovement);

        assertEquals(192f, tank.getBoundingRectangle().x, 0.001f);
        assertEquals(192f, tank.getBoundingRectangle().y, 0.001f);
    }

    @Test
    void cannotTurnWhileMoving() {
        Tank tank = new Tank(new GridPoint2(1, 1), region);

        tank.move(Tank.Direction.UP, emptyField);
        tank.move(Tank.Direction.LEFT, emptyField);

        assertEquals(90f, tank.getRotation(), 0.001f);
    }
}