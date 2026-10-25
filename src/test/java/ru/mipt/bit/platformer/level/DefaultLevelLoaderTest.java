package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultLevelLoaderTest {
    private final LevelData level = new DefaultLevelLoader().load(10, 8);

    @Test
    void containsAiTanks() {
        assertTrue(level.tanks().stream().anyMatch(t -> t.control() == ControlType.RANDOM));
    }

    @Test
    void objectsAreInsideStandardFieldAndDoNotOverlap() {
        List<GridPoint2> positions = new ArrayList<>(level.treePositions());
        level.tanks().forEach(tank -> positions.add(tank.position()));

        positions.forEach(p -> assertTrue(p.x >= 0 && p.x < 10 && p.y >= 0 && p.y < 8));
        assertEquals(positions.size(), new HashSet<>(positions).size());
    }
}