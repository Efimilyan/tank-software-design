package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BulletGunTest {
    private static final float RELOAD = 0.5f;
    private static final GridPosition FROM = new GridPosition(2, 3);
    private static final Blocking SHOOTER_AWAY_FROM_MUZZLE = new Obstacle(new GridPosition(0, 0));

    private final Level level = new Level();
    private final BulletGun gun = new BulletGun(level::add,
            BattleField.of(coordinates -> false, (coordinates, damage) -> { }), 0.1f, 2, RELOAD);

    @Test
    void fireRegistersBulletInLevelAtGivenTile() {
        gun.fire(FROM, Direction.LEFT, SHOOTER_AWAY_FROM_MUZZLE);

        List<GameObject> objects = level.objects();
        assertEquals(1, objects.size());
        Bullet bullet = (Bullet) objects.get(0);
        assertEquals(FROM, bullet.getCurrentCoordinates());
        assertEquals(Direction.LEFT, bullet.getDirection());
        assertEquals(2, bullet.getDamage());
    }

    @Test
    void cannotFireWhileReloading() {
        gun.fire(FROM, Direction.LEFT, SHOOTER_AWAY_FROM_MUZZLE);
        gun.update(RELOAD / 2);

        gun.fire(FROM, Direction.LEFT, SHOOTER_AWAY_FROM_MUZZLE);

        assertEquals(1, level.objects().size());
    }

    @Test
    void canFireAgainAfterReload() {
        gun.fire(FROM, Direction.LEFT, SHOOTER_AWAY_FROM_MUZZLE);
        gun.update(RELOAD);

        gun.fire(FROM, Direction.LEFT, SHOOTER_AWAY_FROM_MUZZLE);

        assertEquals(2, level.objects().size());
    }

    @Test
    void firingAtOccupiedTileHitsItImmediatelyWithoutCreatingBullet() {
        List<String> hits = new ArrayList<>();
        BattleField occupied = new BattleField() {
            @Override
            public boolean isOccupied(GridPosition coordinates) {
                return true;
            }

            @Override
            public void hit(GridPosition coordinates, int damage) {
                hits.add(coordinates + " " + damage);
            }
        };
        BulletGun pointBlank = new BulletGun(level::add, occupied, 0.1f, 2, RELOAD);

        pointBlank.fire(FROM, Direction.LEFT, SHOOTER_AWAY_FROM_MUZZLE);

        assertEquals(List.of("(2, 3) 2"), hits);
        assertTrue(level.objects().isEmpty());
    }

    @Test
    void pointBlankShotAlsoReloadsTheGun() {
        List<String> hits = new ArrayList<>();
        BattleField occupied = new BattleField() {
            @Override
            public boolean isOccupied(GridPosition coordinates) {
                return true;
            }

            @Override
            public void hit(GridPosition coordinates, int damage) {
                hits.add("hit");
            }
        };
        BulletGun pointBlank = new BulletGun(level::add, occupied, 0.1f, 2, RELOAD);

        pointBlank.fire(FROM, Direction.LEFT, SHOOTER_AWAY_FROM_MUZZLE);
        pointBlank.fire(FROM, Direction.LEFT, SHOOTER_AWAY_FROM_MUZZLE);

        assertEquals(1, hits.size());
    }
    @Test
    void firePassesCreatedBulletToSpawner() {
        List<GameObject> spawned = new ArrayList<>();
        BulletGun customGun = new BulletGun(spawned::add,
                BattleField.of(coordinates -> false, (coordinates, damage) -> { }), 0.1f, 2, RELOAD);

        customGun.fire(FROM, Direction.UP, SHOOTER_AWAY_FROM_MUZZLE);

        assertEquals(1, spawned.size());
        assertTrue(spawned.get(0) instanceof Bullet);
    }

    @Test
    void firingIntoTileWithBulletDestroysThatBullet() {
        Bullet bulletInTheWay = new Bullet(FROM, Direction.UP, 0.1f, 2,
                BattleField.of(coordinates -> false, (coordinates, damage) -> { }));
        BulletGun gunNextToBullet = new BulletGun(level::add,
                new GameField(List.of(bulletInTheWay)), 0.1f, 2, RELOAD);

        gunNextToBullet.fire(FROM, Direction.RIGHT, SHOOTER_AWAY_FROM_MUZZLE);

        assertFalse(bulletInTheWay.isAlive());
        assertTrue(level.objects().isEmpty());
    }
    @Test
    void shooterStandingInMuzzleTileIsNotHitAndBulletIsFired() {
        Tank shooter = new Tank(FROM, 0.4f);
        BulletGun shooterGun = new BulletGun(level::add, new GameField(List.of(shooter)), 0.1f, 2, RELOAD);

        shooterGun.fire(FROM, Direction.UP, shooter);

        assertEquals(Tank.DEFAULT_HEALTH, shooter.getHealth());
        assertEquals(1, level.objects().size());
    }
}
