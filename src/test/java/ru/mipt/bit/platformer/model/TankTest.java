package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {
    private static final float SPEED = 0.4f;

    private final GameField emptyField = new GameField(List.of());

    @Test
    void newTankFacesRight() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        assertEquals(0f, tank.getRotation(), 0.001f);
    }

    @Test
    void moveTurnsTankInThatDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        tank.move(Tank.Direction.UP, emptyField);

        assertEquals(90f, tank.getRotation(), 0.001f);
    }

    @Test
    void tankMovesToNextTile() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        tank.move(Tank.Direction.UP, emptyField);

        tank.update(0.4f);
        tank.update(0.1f);

        assertEquals(new GridPoint2(1, 2), tank.getCurrentCoordinates());
    }

    @Test
    void tankDoesNotEnterObstacle() {
        Obstacle obstacle = new Obstacle(new GridPoint2(1, 2));
        GameField field = new GameField(List.of(obstacle));
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        tank.move(Tank.Direction.UP, field);
        tank.update(0.4f);
        tank.update(0.1f);

        assertEquals(new GridPoint2(1, 1), tank.getCurrentCoordinates());
    }

    @Test
    void cannotTurnWhileMoving() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        tank.move(Tank.Direction.UP, emptyField);
        tank.move(Tank.Direction.LEFT, emptyField);

        assertEquals(90f, tank.getRotation(), 0.001f);
    }
}