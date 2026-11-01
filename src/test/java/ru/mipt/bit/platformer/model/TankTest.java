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
    void newTankHasGivenHealth() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 7);

        assertEquals(7, tank.getHealth());
        assertTrue(tank.isAlive());
    }

    @Test
    void newTankFacesRightDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        assertEquals(Direction.RIGHT, tank.getDirection());
    }

    @Test
    void moveRemembersDirectionEvenWhenBlocked() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        tank.move(Direction.UP, coordinates -> true);

        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void damageReducesHealth() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 3);

        tank.takeDamage(1);

        assertEquals(2, tank.getHealth());
        assertTrue(tank.isAlive());
    }

    @Test
    void tankIsDeadWhenHealthRunsOut() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 2);

        tank.takeDamage(2);

        assertFalse(tank.isAlive());
    }

    @Test
    void negativeDamageIsRejected() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 2);

        assertThrows(IllegalArgumentException.class, () -> tank.takeDamage(-1));
        assertEquals(2, tank.getHealth());
    }

    @Test
    void shootFiresIntoTileInFrontOfTank() {
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 3, gun);

        tank.shoot();

        assertEquals(List.of("(2, 1) RIGHT"), gun.shots);
    }

    @Test
    void shootFromMovingTankFiresBeyondItsDestinationTile() {
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 3, gun);
        tank.move(Direction.UP, emptyField);

        tank.shoot();

        assertEquals(List.of("(1, 3) UP"), gun.shots);
    }

    @Test
    void updateReloadsTheGun() {
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 3, gun);

        tank.update(0.25f);

        assertEquals(0.25f, gun.updated, 0.001f);
    }

    @Test
    void tankWithoutGunCanShootHarmlessly() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        tank.shoot();

        assertTrue(tank.isAlive());
    }

    @Test
    void moveTurnsTankInThatDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        tank.move(Direction.UP, emptyField);

        assertEquals(90f, tank.getRotation(), 0.001f);
    }

    @Test
    void tankMovesToNextTile() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        tank.move(Direction.UP, emptyField);

        tank.update(0.4f);
        tank.update(0.1f);

        assertEquals(new GridPoint2(1, 2), tank.getCurrentCoordinates());
    }

    @Test
    void tankDoesNotEnterObstacle() {
        Obstacle obstacle = new Obstacle(new GridPoint2(1, 2));
        GameField field = new GameField(List.of(obstacle));
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        tank.move(Direction.UP, field);
        tank.update(0.4f);
        tank.update(0.1f);

        assertEquals(new GridPoint2(1, 1), tank.getCurrentCoordinates());
    }

    @Test
    void cannotTurnWhileMoving() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        tank.move(Direction.UP, emptyField);
        tank.move(Direction.LEFT, emptyField);

        assertEquals(90f, tank.getRotation(), 0.001f);
    }
    @Test
    void idleTankOccupiesOnlyItsTile() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);

        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertFalse(tank.occupies(new GridPoint2(1, 2)));
    }

    @Test
    void movingTankOccupiesBothTiles() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        tank.move(Direction.UP, emptyField);

        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(1, 2)));
        assertFalse(tank.occupies(new GridPoint2(1, 3)));
    }

    @Test
    void tankReleasesOldTileWhenMoveFinished() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        tank.move(Direction.UP, emptyField);
        tank.update(0.4f);
        tank.update(0.1f);

        assertFalse(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(1, 2)));
    }

    @Test
    void blockedTankOccupiesOnlyItsTile() {
        GameField field = new GameField(List.of(new Obstacle(new GridPoint2(1, 2))));
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        tank.move(Direction.UP, field);

        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertFalse(tank.occupies(new GridPoint2(1, 2)));
    }
}
