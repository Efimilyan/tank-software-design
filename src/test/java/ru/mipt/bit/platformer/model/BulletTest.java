package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BulletTest {
    private static final float SPEED = 0.1f;
    private static final int DAMAGE = 2;
    private static final GridPoint2 START = new GridPoint2(3, 3);

    private final Bullet bullet = new Bullet(START, Direction.UP, SPEED, DAMAGE);

    @Test
    void newBulletStandsInStartTileWithGivenParameters() {
        assertEquals(START, bullet.getCurrentCoordinates());
        assertEquals(START, bullet.getDestinationCoordinates());
        assertEquals(Direction.UP, bullet.getDirection());
        assertEquals(DAMAGE, bullet.getDamage());
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
    }

    @Test
    void bulletRotationFollowsDirection() {
        assertEquals(Direction.UP.rotation(), bullet.getRotation(), 0.001f);
    }
}
