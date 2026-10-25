package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RandomLevelGeneratorTest {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;
    private static final int ENEMY_COUNT = 3;

    private LevelData generate(long seed, int width, int height, int enemyCount) {
        return new RandomLevelGenerator(new Random(seed), enemyCount).load(width, height);
    }

    private List<GridPoint2> allPositions(LevelData level) {
        List<GridPoint2> positions = new ArrayList<>();
        level.tanks().forEach(tank -> positions.add(tank.position()));
        positions.addAll(level.treePositions());
        return positions;
    }

    @Test
    void allObjectsAreInsideField() {
        for (int seed = 0; seed < 100; seed++) {
            LevelData level = generate(seed, WIDTH, HEIGHT, ENEMY_COUNT);

            allPositions(level).forEach(position -> assertTrue(isInside(position)));
        }
    }

    @Test
    void objectsDoNotOverlap() {
        for (int seed = 0; seed < 100; seed++) {
            List<GridPoint2> positions = allPositions(generate(seed, WIDTH, HEIGHT, ENEMY_COUNT));

            assertEquals(positions.size(), new HashSet<>(positions).size());
        }
    }

    @Test
    void hasOnePlayerAndRequestedNumberOfAiTanks() {
        LevelData level = generate(1, WIDTH, HEIGHT, ENEMY_COUNT);

        long players = level.tanks().stream().filter(t -> t.control() == ControlType.PLAYER).count();
        long enemies = level.tanks().stream().filter(t -> t.control() == ControlType.RANDOM).count();

        assertEquals(1, players);
        assertEquals(ENEMY_COUNT, enemies);
    }

    @Test
    void treeCountDependsOnFieldSize() {
        for (int seed = 0; seed < 100; seed++) {
            LevelData level = generate(seed, 4, 4, 0);

            assertTrue(level.treePositions().size() <= 4);
        }
    }

    @Test
    void fullFieldLeavesNoRoomForTrees() {
        LevelData level = generate(3, 4, 4, 15);

        assertEquals(16, level.tanks().size());
        assertTrue(level.treePositions().isEmpty());
    }

    @Test
    void moreTanksThanCellsIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> generate(1, 4, 4, 16));
    }

    @Test
    void negativeEnemyCountIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> generate(1, WIDTH, HEIGHT, -1));
    }

    @Test
    void sameSeedGivesSameLevel() {
        assertEquals(generate(7, WIDTH, HEIGHT, ENEMY_COUNT), generate(7, WIDTH, HEIGHT, ENEMY_COUNT));
    }

    private boolean isInside(GridPoint2 p) {
        return p.x >= 0 && p.x < WIDTH && p.y >= 0 && p.y < HEIGHT;
    }
}