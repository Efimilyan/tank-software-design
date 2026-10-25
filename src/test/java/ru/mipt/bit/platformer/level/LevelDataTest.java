package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LevelDataTest {
    private static final TankSpec PLAYER = new TankSpec(new GridPoint2(0, 0), ControlType.PLAYER);
    private static final TankSpec ENEMY = new TankSpec(new GridPoint2(1, 1), ControlType.RANDOM);

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
        TankSpec secondPlayer = new TankSpec(new GridPoint2(2, 2), ControlType.PLAYER);

        assertThrows(IllegalArgumentException.class,
                () -> new LevelData(List.of(PLAYER, secondPlayer), List.of()));
    }

    @Test
    void levelKeepsCopyOfGivenLists() {
        List<TankSpec> tanks = new ArrayList<>(List.of(PLAYER));
        List<GridPoint2> trees = new ArrayList<>();
        LevelData level = new LevelData(tanks, trees);

        tanks.add(ENEMY);
        trees.add(new GridPoint2(3, 3));

        assertEquals(1, level.tanks().size());
        assertTrue(level.treePositions().isEmpty());
    }
}