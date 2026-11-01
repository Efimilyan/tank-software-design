package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletTest {
    private static final float SPEED = 0.1f;
    private static final int DAMAGE = 2;
    private static final GridPoint2 START = new GridPoint2(3, 3);

    private static final CollisionChecker FREE_FIELD = coordinates -> false;

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

        assertEquals(new GridPoint2(3, 4), bullet.getDestinationCoordinates());
        assertEquals(START, bullet.getCurrentCoordinates());
        assertEquals(0.5f, bullet.getMovementProgress(), 0.001f);
    }

    @Test
    void bulletArrivesInNextTileAfterOneTileTime() {
        bullet.update(SPEED);

        assertEquals(new GridPoint2(3, 4), bullet.getCurrentCoordinates());
    }

    @Test
    void bulletKeepsFlyingTileAfterTile() {
        bullet.update(SPEED);
        bullet.update(SPEED);
        bullet.update(SPEED);

        assertEquals(new GridPoint2(3, 6), bullet.getCurrentCoordinates());
        assertTrue(bullet.isAlive());
    }

    @Test
    void bulletRotationFollowsDirection() {
        assertEquals(Direction.UP.rotation(), bullet.getRotation(), 0.001f);
    }

    @Test
    void bulletOccupiesCurrentAndDestinationTiles() {
        bullet.update(SPEED / 2);

        assertTrue(bullet.occupies(new GridPoint2(3, 3)));
        assertTrue(bullet.occupies(new GridPoint2(3, 4)));
        assertFalse(bullet.occupies(new GridPoint2(3, 5)));
    }

    @Test
    void bulletHitsNextTileAndDiesWhenItIsOccupied() {
        List<String> hits = new ArrayList<>();
        CollisionChecker wallAbove = new CollisionChecker() {
            @Override
            public boolean isOccupied(GridPoint2 coordinates) {
                return coordinates.equals(new GridPoint2(3, 4));
            }

            @Override
            public void hit(GridPoint2 coordinates, int damage) {
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
        Bullet blocked = new Bullet(START, Direction.UP, SPEED, DAMAGE, coordinates -> true);
        blocked.update(SPEED);

        blocked.update(SPEED);

        assertEquals(START, blocked.getDestinationCoordinates());
    }
}
