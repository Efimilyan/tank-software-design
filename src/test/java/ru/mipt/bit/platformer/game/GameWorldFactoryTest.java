package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.level.ControlType;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.TankSpec;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelListener;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameWorldFactoryTest {
    private static final float SPEED = 0.4f;
    private static final int HEALTH = 5;
    private static final GameSettings SETTINGS = new GameSettings(SPEED, HEALTH, 0.1f, 2, 0.5f, 0.0);

    private static Random alwaysChoosing(Direction direction) {
        return new Random() {
            @Override
            public int nextInt(int bound) {
                return direction.ordinal();
            }
        };
    }

    private static ControlStrategy alwaysGoing(Direction direction) {
        return () -> new MoveCommand(direction);
    }

    private static TankSpec player(int x, int y) {
        return new TankSpec(new GridPoint2(x, y), ControlType.PLAYER);
    }

    private static TankSpec enemy(int x, int y) {
        return new TankSpec(new GridPoint2(x, y), ControlType.RANDOM);
    }

    private record Assembly(Level level, List<Tank> tanks, List<Obstacle> obstacles) {
    }

    private static Assembly assemble(LevelData data, Direction playerGoes, Direction enemyGoes,
                                     int width, int height) {
        GameWorldFactory factory =
                new GameWorldFactory(SETTINGS, alwaysGoing(playerGoes), alwaysChoosing(enemyGoes));
        Level level = new Level();
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
        factory.populate(level, data, width, height);
        return new Assembly(level, tanks, obstacles);
    }

    private static void runFor(Assembly assembly, int updates) {
        for (int i = 0; i < updates; i++) {
            assembly.level().update(0.1f);
        }
    }

    @Test
    void createsTankForEachSpecAndObstacleForEachTree() {
        LevelData level = new LevelData(
                List.of(player(1, 1), enemy(5, 5)),
                List.of(new GridPoint2(3, 3), new GridPoint2(4, 4)));

        Assembly assembly = assemble(level, Direction.UP, Direction.UP, 10, 8);

        assertEquals(2, assembly.tanks().size());
        assertEquals(2, assembly.obstacles().size());
        assertEquals(new GridPoint2(1, 1), assembly.tanks().get(0).getCurrentCoordinates());
        assertEquals(new GridPoint2(5, 5), assembly.tanks().get(1).getCurrentCoordinates());
        assertEquals(new GridPoint2(3, 3), assembly.obstacles().get(0).getCoordinates());
    }

    @Test
    void playerAndAiTanksFollowTheirOwnStrategies() {
        LevelData level = new LevelData(List.of(player(1, 1), enemy(5, 5)), List.of());
        Assembly assembly = assemble(level, Direction.UP, Direction.RIGHT, 10, 8);

        runFor(assembly, 4);

        assertEquals(new GridPoint2(1, 2), assembly.tanks().get(0).getCurrentCoordinates());
        assertEquals(new GridPoint2(6, 5), assembly.tanks().get(1).getCurrentCoordinates());
    }

    @Test
    void tankCannotLeaveField() {
        LevelData level = new LevelData(List.of(player(0, 0), enemy(4, 4)), List.of());
        Assembly assembly = assemble(level, Direction.LEFT, Direction.UP, 5, 5);

        runFor(assembly, 10);

        assertEquals(new GridPoint2(0, 0), assembly.tanks().get(0).getCurrentCoordinates());
        assertEquals(new GridPoint2(4, 4), assembly.tanks().get(1).getCurrentCoordinates());
    }

    @Test
    void treeBlocksTank() {
        LevelData level = new LevelData(
                List.of(player(1, 1), enemy(8, 7)), List.of(new GridPoint2(1, 2)));
        Assembly assembly = assemble(level, Direction.UP, Direction.UP, 10, 8);

        runFor(assembly, 10);

        assertEquals(new GridPoint2(1, 1), assembly.tanks().get(0).getCurrentCoordinates());
    }

    @Test
    void standingTankBlocksAnotherTank() {
        LevelData level = new LevelData(List.of(player(1, 1), enemy(2, 1)), List.of());
        Assembly assembly = assemble(level, Direction.RIGHT, Direction.UP, 5, 2);

        runFor(assembly, 10);

        assertEquals(new GridPoint2(1, 1), assembly.tanks().get(0).getCurrentCoordinates());
        assertEquals(new GridPoint2(2, 1), assembly.tanks().get(1).getCurrentCoordinates());
    }

    @Test
    void movingTankKeepsBothTilesOccupied() {
        LevelData level = new LevelData(List.of(player(1, 1), enemy(0, 1)), List.of());
        Assembly assembly = assemble(level, Direction.RIGHT, Direction.RIGHT, 10, 8);

        runFor(assembly, 1);

        assertEquals(new GridPoint2(2, 1), assembly.tanks().get(0).getDestinationCoordinates());
        assertEquals(new GridPoint2(0, 1), assembly.tanks().get(1).getDestinationCoordinates());
    }

    @Test
    void tanksGetHealthFromFactory() {
        LevelData level = new LevelData(List.of(player(1, 1), enemy(5, 5)), List.of());

        Assembly assembly = assemble(level, Direction.UP, Direction.UP, 10, 8);

        assertEquals(HEALTH, assembly.tanks().get(0).getHealth());
        assertEquals(HEALTH, assembly.tanks().get(1).getHealth());
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
        assertEquals(new GridPoint2(2, 1), ((Bullet) bullets.get(0)).getCurrentCoordinates());
        assertEquals(2, ((Bullet) bullets.get(0)).getDamage());
    }

    private static Assembly assembleIdle(LevelData data, int width, int height) {
        GameWorldFactory factory = new GameWorldFactory(
                SETTINGS, () -> Command.NONE, alwaysChoosing(Direction.UP));
        Level level = new Level();
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
        factory.populate(level, data, width, height);
        return new Assembly(level, tanks, List.of());
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
        assertEquals(1, countOf(assembly.level(), TankController.class));
    }

    @Test
    void treeStopsBullet() {
        LevelData data = new LevelData(
                List.of(player(1, 1), enemy(6, 1)), List.of(new GridPoint2(3, 1)));
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
