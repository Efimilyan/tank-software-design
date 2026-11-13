package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultLevelProviderTest {
    private final LevelData level = new DefaultLevelProvider().load(10, 8);

    @Test
    void containsAiTanks() {
        assertTrue(level.tanks().stream().anyMatch(t -> t.control() == ControlType.AI));
    }

    @Test
    void objectsAreInsideStandardFieldAndDoNotOverlap() {
        List<GridPosition> positions = new ArrayList<>(level.treePositions());
        level.tanks().forEach(tank -> positions.add(tank.position()));

        positions.forEach(p -> assertTrue(p.x() >= 0 && p.x() < 10 && p.y() >= 0 && p.y() < 8));
        assertEquals(positions.size(), new HashSet<>(positions).size());
    }
}