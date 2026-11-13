package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelDataTest {
    private static final TankSpec PLAYER = new TankSpec(new GridPosition(0, 0), ControlType.HUMAN);
    private static final TankSpec ENEMY = new TankSpec(new GridPosition(1, 1), ControlType.AI);

    @Test
    void levelWithOnePlayerAndEnemiesIsAccepted() {
        LevelData level = new LevelData(List.of(PLAYER, ENEMY), List.of());

        assertEquals(2, level.tanks().size());
    }

    @Test
    void levelWithoutPlayerIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new LevelData(List.of(ENEMY), List.of()));
    }

    @Test
    void levelWithTwoPlayersIsRejected() {
        TankSpec secondPlayer = new TankSpec(new GridPosition(2, 2), ControlType.HUMAN);

        assertThrows(IllegalArgumentException.class,
                () -> new LevelData(List.of(PLAYER, secondPlayer), List.of()));
    }

    @Test
    void levelKeepsCopyOfGivenLists() {
        List<TankSpec> tanks = new ArrayList<>(List.of(PLAYER));
        List<GridPosition> trees = new ArrayList<>();
        LevelData level = new LevelData(tanks, trees);

        tanks.add(ENEMY);
        trees.add(new GridPosition(3, 3));

        assertEquals(1, level.tanks().size());
        assertTrue(level.treePositions().isEmpty());
    }
}