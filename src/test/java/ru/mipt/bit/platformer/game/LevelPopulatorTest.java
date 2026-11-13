package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.*;
import ru.mipt.bit.platformer.level.ControlType;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.TankSpec;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.model.api.Direction;
import ru.mipt.bit.platformer.model.api.GameObject;
import ru.mipt.bit.platformer.model.api.GridPosition;
import ru.mipt.bit.platformer.model.api.LevelListener;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LevelPopulatorTest {
    private static final float SPEED = 0.4f;
    private static final int HEALTH = 5;
    private static final GameSettings SETTINGS =
            new GameSettings(SPEED, OptionalInt.of(HEALTH), BulletType.BASE_BULLET, 0.5f, 0.0);

    private static Random alwaysChoosing(Direction direction) {
        return new Random() {
            @Override
            public int nextInt(int bound) {
                return direction.ordinal();
            }
        };
    }

    private static ControlStrategy alwaysGoing(Direction direction) {
        return deltaTime -> new MoveCommand(direction);
    }

    private static ControlStrategyRegistry strategies(ControlStrategy player, Random random) {
        return new ControlStrategyRegistry()
                .register(ControlType.HUMAN, () -> player)
                .register(ControlType.AI, () -> new RandomControlStrategy(
                        random, SETTINGS.enemyShotsPerSecond()));
    }

    private static TankSpec player(int x, int y) {
        return new TankSpec(new GridPosition(x, y), ControlType.HUMAN);
    }

    private static TankSpec enemy(int x, int y) {
        return new TankSpec(new GridPosition(x, y), ControlType.AI);
    }

    private record Assembly(Level level, Controllers controllers,
                            List<Tank> tanks, List<Obstacle> obstacles) {
    }

    private static Assembly assemble(LevelData data, Direction playerGoes, Direction enemyGoes,
                                     int width, int height) {
        LevelPopulator factory = new LevelPopulator(
                SETTINGS, strategies(alwaysGoing(playerGoes), alwaysChoosing(enemyGoes)), () -> HEALTH);
        Level level = new Level();
        Controllers controllers = new Controllers();
        List<Tank> tanks = new ArrayList<>();
        List<Obstacle> obstacles = new ArrayList<>();
        level.subscribe(new LevelListener() {
            @Override
            public void objectAdded(GameObject object) {
                if (object instanceof Tank tank) {
                    tanks.add(tank);
                } else if (object instanceof Obstacle obstacle) {
                    obstacles.add(obstacle);
                }
            }

            @Override
            public void objectRemoved(GameObject object) {
            }
        });
        factory.populate(level, controllers, data, width, height);
        return new Assembly(level, controllers, tanks, obstacles);
    }

    private static void runFor(Assembly assembly, int updates) {
        for (int i = 0; i < updates; i++) {
            assembly.controllers().update(0.1f);
            assembly.level().update(0.1f);
        }
    }

    @Test
    void createsTankForEachSpecAndObstacleForEachTree() {
        LevelData level = new LevelData(
                List.of(player(1, 1), enemy(5, 5)),
                List.of(new GridPosition(3, 3), new GridPosition(4, 4)));

        Assembly assembly = assemble(level, Direction.UP, Direction.UP, 10, 8);

        assertEquals(2, assembly.tanks().size());
        assertEquals(2, assembly.obstacles().size());
        assertEquals(new GridPosition(1, 1), assembly.tanks().get(0).getCurrentCoordinates());
        assertEquals(new GridPosition(5, 5), assembly.tanks().get(1).getCurrentCoordinates());
        assertEquals(new GridPosition(3, 3), assembly.obstacles().get(0).getCoordinates());
    }

    @Test
    void playerAndAiTanksFollowTheirOwnStrategies() {
        LevelData level = new LevelData(List.of(player(1, 1), enemy(5, 5)), List.of());
        Assembly assembly = assemble(level, Direction.UP, Direction.RIGHT, 10, 8);

        runFor(assembly, 4);

        assertEquals(new GridPosition(1, 2), assembly.tanks().get(0).getCurrentCoordinates());
        assertEquals(new GridPosition(6, 5), assembly.tanks().get(1).getCurrentCoordinates());
    }

    @Test
    void tankCannotLeaveField() {
        LevelData level = new LevelData(List.of(player(0, 0), enemy(4, 4)), List.of());
        Assembly assembly = assemble(level, Direction.LEFT, Direction.UP, 5, 5);

        runFor(assembly, 10);

        assertEquals(new GridPosition(0, 0), assembly.tanks().get(0).getCurrentCoordinates());
        assertEquals(new GridPosition(4, 4), assembly.tanks().get(1).getCurrentCoordinates());
    }

    @Test
    void treeBlocksTank() {
        LevelData level = new LevelData(
                List.of(player(1, 1), enemy(8, 7)), List.of(new GridPosition(1, 2)));
        Assembly assembly = assemble(level, Direction.UP, Direction.UP, 10, 8);

        runFor(assembly, 10);

        assertEquals(new GridPosition(1, 1), assembly.tanks().get(0).getCurrentCoordinates());
    }

    @Test
    void standingTankBlocksAnotherTank() {
        LevelData level = new LevelData(List.of(player(1, 1), enemy(2, 1)), List.of());
        Assembly assembly = assemble(level, Direction.RIGHT, Direction.UP, 5, 2);

        runFor(assembly, 10);

        assertEquals(new GridPosition(1, 1), assembly.tanks().get(0).getCurrentCoordinates());
        assertEquals(new GridPosition(2, 1), assembly.tanks().get(1).getCurrentCoordinates());
    }

    @Test
    void movingTankKeepsBothTilesOccupied() {
        LevelData level = new LevelData(List.of(player(1, 1), enemy(0, 1)), List.of());
        Assembly assembly = assemble(level, Direction.RIGHT, Direction.RIGHT, 10, 8);

        runFor(assembly, 1);

        assertEquals(new GridPosition(2, 1), assembly.tanks().get(0).getDestinationCoordinates());
        assertEquals(new GridPosition(0, 1), assembly.tanks().get(1).getDestinationCoordinates());
    }

    @Test
    void tanksGetHealthFromFactory() {
        LevelData level = new LevelData(List.of(player(1, 1), enemy(5, 5)), List.of());

        Assembly assembly = assemble(level, Direction.UP, Direction.UP, 10, 8);

        assertEquals(HEALTH, assembly.tanks().get(0).getHealth());
        assertEquals(HEALTH, assembly.tanks().get(1).getHealth());
    }

    @Test
    void eachTankTakesItsOwnHealthFromSupplier() {
        List<Integer> values = new ArrayList<>(List.of(70, 90));
        LevelPopulator populator = new LevelPopulator(
                SETTINGS,
                strategies(alwaysGoing(Direction.UP), alwaysChoosing(Direction.UP)),
                () -> values.remove(0));
        Level level = new Level();
        LevelData data = new LevelData(List.of(player(1, 1), enemy(5, 5)), List.of());

        populator.populate(level, new Controllers(), data, 10, 8);

        List<Integer> healths = level.objects().stream()
                .filter(Tank.class::isInstance)
                .map(Tank.class::cast)
                .map(Tank::getHealth)
                .toList();
        assertEquals(List.of(70, 90), healths);
    }
    @Test
    void tankShootsBulletIntoLevel() {
        LevelData level = new LevelData(List.of(player(1, 1)), List.of());
        Assembly assembly = assemble(level, Direction.UP, Direction.UP, 10, 8);

        assembly.tanks().get(0).shoot();

        List<GameObject> bullets = assembly.level().objects().stream()
                .filter(object -> object instanceof Bullet)
                .toList();
        assertEquals(1, bullets.size());
        assertEquals(new GridPosition(2, 1), ((Bullet) bullets.get(0)).getCurrentCoordinates());
        assertEquals(2, ((Bullet) bullets.get(0)).getDamage());
    }

    private static Assembly assembleIdle(LevelData data, int width, int height) {
        LevelPopulator factory = new LevelPopulator(
                SETTINGS, strategies((deltaTime) -> TankCommand.NONE, alwaysChoosing(Direction.UP)), () -> HEALTH);
        Level level = new Level();
        Controllers controllers = new Controllers();
        List<Tank> tanks = new ArrayList<>();
        level.subscribe(new LevelListener() {
            @Override
            public void objectAdded(GameObject object) {
                if (object instanceof Tank tank) {
                    tanks.add(tank);
                }
            }

            @Override
            public void objectRemoved(GameObject object) {
            }
        });
        factory.populate(level, controllers, data, width, height);
        return new Assembly(level, controllers, tanks, List.of());
    }

    private static long countOf(Level level, Class<?> type) {
        return level.objects().stream().filter(type::isInstance).count();
    }

    @Test
    void bulletHitsEnemyTankAndDisappears() {
        LevelData data = new LevelData(List.of(player(1, 1), enemy(4, 1)), List.of());
        Assembly assembly = assembleIdle(data, 10, 2);

        assembly.tanks().get(0).shoot();
        runFor(assembly, 10);

        assertEquals(HEALTH - 2, assembly.tanks().get(1).getHealth());
        assertEquals(0, countOf(assembly.level(), Bullet.class));
    }

    @Test
    void enemyTankIsRemovedFromLevelTogetherWithItsControllerWhenHealthRunsOut() {
        LevelData data = new LevelData(List.of(player(1, 1), enemy(4, 1)), List.of());
        Assembly assembly = assembleIdle(data, 10, 2);
        Tank enemyTank = assembly.tanks().get(1);

        for (int shot = 0; shot < 3; shot++) {
            assembly.tanks().get(0).shoot();
            runFor(assembly, 7);
        }

        assertFalse(enemyTank.isAlive());
        assertFalse(assembly.level().objects().contains(enemyTank));
        assertEquals(1, countOf(assembly.level(), Tank.class));
        assertEquals(1, assembly.controllers().size());
    }

    @Test
    void treeStopsBullet() {
        LevelData data = new LevelData(
                List.of(player(1, 1), enemy(6, 1)), List.of(new GridPosition(3, 1)));
        Assembly assembly = assembleIdle(data, 10, 2);

        assembly.tanks().get(0).shoot();
        runFor(assembly, 10);

        assertEquals(HEALTH, assembly.tanks().get(1).getHealth());
        assertEquals(0, countOf(assembly.level(), Bullet.class));
        assertEquals(1, countOf(assembly.level(), Obstacle.class));
    }

    @Test
    void bulletDisappearsAtFieldEdge() {
        LevelData data = new LevelData(List.of(player(1, 0), enemy(5, 1)), List.of());
        Assembly assembly = assembleIdle(data, 4, 2);

        assembly.tanks().get(0).shoot();
        runFor(assembly, 10);

        assertEquals(0, countOf(assembly.level(), Bullet.class));
    }

    @Test
    void shotInFrontOfFieldEdgeCreatesNoBullet() {
        LevelData data = new LevelData(List.of(player(3, 0), enemy(0, 1)), List.of());
        Assembly assembly = assembleIdle(data, 4, 2);

        assembly.tanks().get(0).shoot();

        assertEquals(0, countOf(assembly.level(), Bullet.class));
    }

    @Test
    void enemyStandingRightInFrontIsHitAtOnce() {
        LevelData data = new LevelData(List.of(player(1, 1), enemy(2, 1)), List.of());
        Assembly assembly = assembleIdle(data, 10, 2);

        assembly.tanks().get(0).shoot();

        assertEquals(HEALTH - 2, assembly.tanks().get(1).getHealth());
        assertEquals(0, countOf(assembly.level(), Bullet.class));
    }

    @Test
    void collidingBulletsDestroyEachOther() {
        LevelData data = new LevelData(List.of(player(1, 1), enemy(6, 1)), List.of());
        Assembly assembly = assembleIdle(data, 10, 2);
        Tank player = assembly.tanks().get(0);
        Tank enemy = assembly.tanks().get(1);
        enemy.move(Direction.LEFT, coordinates -> true);

        player.shoot();
        enemy.shoot();
        runFor(assembly, 10);

        assertEquals(0, countOf(assembly.level(), Bullet.class));
        assertEquals(HEALTH, player.getHealth());
        assertEquals(HEALTH, enemy.getHealth());
    }
}
