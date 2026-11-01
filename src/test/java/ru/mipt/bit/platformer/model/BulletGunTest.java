package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletGunTest {
    private static final float RELOAD = 0.5f;
    private static final GridPoint2 FROM = new GridPoint2(2, 3);

    private final Level level = new Level();
    private final BulletGun gun = new BulletGun(level, coordinates -> false, 0.1f, 2, RELOAD);

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
    void firingAtOccupiedTileHitsItImmediatelyWithoutCreatingBullet() {
        List<String> hits = new ArrayList<>();
        CollisionChecker occupied = new CollisionChecker() {
            @Override
            public boolean isOccupied(GridPoint2 coordinates) {
                return true;
            }

            @Override
            public void hit(GridPoint2 coordinates, int damage) {
                hits.add(coordinates + " " + damage);
            }
        };
        BulletGun pointBlank = new BulletGun(level, occupied, 0.1f, 2, RELOAD);

        pointBlank.fire(FROM, Direction.LEFT);

        assertEquals(List.of("(2, 3) 2"), hits);
        assertTrue(level.objects().isEmpty());
    }

    @Test
    void pointBlankShotAlsoReloadsTheGun() {
        List<String> hits = new ArrayList<>();
        CollisionChecker occupied = new CollisionChecker() {
            @Override
            public boolean isOccupied(GridPoint2 coordinates) {
                return true;
            }

            @Override
            public void hit(GridPoint2 coordinates, int damage) {
                hits.add("hit");
            }
        };
        BulletGun pointBlank = new BulletGun(level, occupied, 0.1f, 2, RELOAD);

        pointBlank.fire(FROM, Direction.LEFT);
        pointBlank.fire(FROM, Direction.LEFT);

        assertEquals(1, hits.size());
    }
}
