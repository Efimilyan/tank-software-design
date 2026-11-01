package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletGunTest {
    private static final float RELOAD = 0.5f;
    private static final GridPoint2 FROM = new GridPoint2(2, 3);

    private final Level level = new Level();
    private final BulletGun gun = new BulletGun(level, 0.1f, 2, RELOAD);

    @Test
    void fireRegistersBulletInLevelAtGivenTile() {
        gun.fire(FROM, Direction.LEFT);

        List<GameObject> objects = level.objects();
        assertEquals(1, objects.size());
        Bullet bullet = (Bullet) objects.get(0);
        assertEquals(FROM, bullet.getCurrentCoordinates());
        assertEquals(Direction.LEFT, bullet.getDirection());
        assertEquals(2, bullet.getDamage());
    }

    @Test
    void cannotFireWhileReloading() {
        gun.fire(FROM, Direction.LEFT);
        gun.update(RELOAD / 2);

        gun.fire(FROM, Direction.LEFT);

        assertEquals(1, level.objects().size());
    }

    @Test
    void canFireAgainAfterReload() {
        gun.fire(FROM, Direction.LEFT);
        gun.update(RELOAD);

        gun.fire(FROM, Direction.LEFT);

        assertEquals(2, level.objects().size());
    }

    @Test
    void newGunIsReadyToFire() {
        gun.fire(FROM, Direction.UP);

        assertTrue(!level.objects().isEmpty());
    }
}
