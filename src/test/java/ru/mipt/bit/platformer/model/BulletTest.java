package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.BattleField;
import ru.mipt.bit.platformer.model.api.Direction;
import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {
    private static final float SPEED = 0.1f;
    private static final int DAMAGE = 2;
    private static final GridPosition START = new GridPosition(3, 3);

    private static final BattleField FREE_FIELD =
            BattleField.of(coordinates -> false, (coordinates, damage) -> { });

    private final Bullet bullet = new Bullet(START, Direction.UP, SPEED, DAMAGE, FREE_FIELD);

    @Test
    void newBulletStandsInStartTileWithGivenParameters() {
        assertEquals(START, bullet.getCurrentCoordinates());
        assertEquals(START, bullet.getDestinationCoordinates());
        assertEquals(Direction.UP, bullet.getDirection());
        assertEquals(DAMAGE, bullet.getDamage());
        assertTrue(bullet.isAlive());
    }

    @Test
    void bulletStartsFlyingToNextTileInItsDirection() {
        bullet.update(SPEED / 2);

        assertEquals(new GridPosition(3, 4), bullet.getDestinationCoordinates());
        assertEquals(START, bullet.getCurrentCoordinates());
        assertEquals(0.5f, bullet.getMovementProgress(), 0.001f);
    }

    @Test
    void bulletArrivesInNextTileAfterOneTileTime() {
        bullet.update(SPEED);

        assertEquals(new GridPosition(3, 4), bullet.getCurrentCoordinates());
    }

    @Test
    void bulletKeepsFlyingTileAfterTile() {
        bullet.update(SPEED);
        bullet.update(SPEED);
        bullet.update(SPEED);

        assertEquals(new GridPosition(3, 6), bullet.getCurrentCoordinates());
        assertTrue(bullet.isAlive());
    }

    @Test
    void bulletRotationFollowsDirection() {
        assertEquals(Direction.UP.rotation(), bullet.getRotation(), 0.001f);
    }

    @Test
    void bulletOccupiesCurrentAndDestinationTiles() {
        bullet.update(SPEED / 2);

        assertTrue(bullet.occupies(new GridPosition(3, 3)));
        assertTrue(bullet.occupies(new GridPosition(3, 4)));
        assertFalse(bullet.occupies(new GridPosition(3, 5)));
    }

    @Test
    void bulletHitsNextTileAndDiesWhenItIsOccupied() {
        List<String> hits = new ArrayList<>();
        BattleField wallAbove = new BattleField() {
            @Override
            public boolean isOccupied(GridPosition coordinates) {
                return coordinates.equals(new GridPosition(3, 4));
            }

            @Override
            public void hit(GridPosition coordinates, int damage) {
                hits.add(coordinates + " " + damage);
            }
        };
        Bullet blocked = new Bullet(START, Direction.UP, SPEED, DAMAGE, wallAbove);

        blocked.update(SPEED);

        assertEquals(List.of("(3, 4) 2"), hits);
        assertFalse(blocked.isAlive());
        assertEquals(START, blocked.getCurrentCoordinates());
    }

    @Test
    void bulletThatHitSomethingStopsMoving() {
        Bullet blocked = new Bullet(START, Direction.UP, SPEED, DAMAGE,
                BattleField.of(coordinates -> true, (coordinates, damage) -> { }));
        blocked.update(SPEED);

        blocked.update(SPEED);

        assertEquals(START, blocked.getDestinationCoordinates());
    }

    @Test
    void bulletDiesWhenHit() {
        bullet.hit(DAMAGE);

        assertFalse(bullet.isAlive());
    }

    @Test
    void bulletFlyingIntoAnotherBulletDestroysBoth() {
        Bullet target = new Bullet(new GridPosition(3, 4), Direction.DOWN, SPEED, DAMAGE, FREE_FIELD);
        Bullet shooter = new Bullet(START, Direction.UP, SPEED, DAMAGE, new GameField(List.of(target)));

        shooter.update(SPEED);

        assertFalse(shooter.isAlive());
        assertFalse(target.isAlive());
    }
}
