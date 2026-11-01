package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.level.ControlType;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.TankSpec;
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

class GameWorldFactoryTest {
    private static final float SPEED = 0.4f;

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
                new GameWorldFactory(SPEED, alwaysGoing(playerGoes), alwaysChoosing(enemyGoes));
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
}