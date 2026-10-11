package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomLevelGeneratorTest {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;

    @Test
    void allObjectsAreInsideField() {
        for (int seed = 0; seed < 100; seed++) {
            LevelData level = new RandomLevelGenerator(new Random(seed)).load(WIDTH, HEIGHT);

            assertTrue(isInside(level.playerPosition()));
            level.treePositions().forEach(tree -> assertTrue(isInside(tree)));
        }
    }

    @Test
    void objectsDoNotOverlap() {
        for (int seed = 0; seed < 100; seed++) {
            LevelData level = new RandomLevelGenerator(new Random(seed)).load(WIDTH, HEIGHT);
            Set<GridPoint2> trees = new HashSet<>(level.treePositions());

            assertEquals(level.treePositions().size(), trees.size());
            assertFalse(trees.contains(level.playerPosition()));
        }
    }

    @Test
    void treeCountDependsOnFieldSize() {
        for (int seed = 0; seed < 100; seed++) {
            LevelData level = new RandomLevelGenerator(new Random(seed)).load(4, 4);

            assertTrue(level.treePositions().size() <= 4);
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        LevelData first = new RandomLevelGenerator(new Random(7)).load(WIDTH, HEIGHT);
        LevelData second = new RandomLevelGenerator(new Random(7)).load(WIDTH, HEIGHT);

        assertEquals(first, second);
    }

    private boolean isInside(GridPoint2 p) {
        return p.x >= 0 && p.x < WIDTH && p.y >= 0 && p.y < HEIGHT;
    }
}