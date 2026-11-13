package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.BattleField;
import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattleFieldTest {
    private static final GridPosition WALL = new GridPosition(1, 1);

    private final List<String> hits = new ArrayList<>();
    private final BattleField field = BattleField.of(
            coordinates -> coordinates.equals(WALL),
            (coordinates, damage) -> hits.add(coordinates + " " + damage));

    @Test
    void occupiedQuestionGoesToChecker() {
        assertTrue(field.isOccupied(WALL));
        assertFalse(field.isOccupied(new GridPosition(2, 2)));
    }

    @Test
    void hitGoesToReceiver() {
        field.hit(WALL, 3);

        assertEquals(List.of("(1, 1) 3"), hits);
    }
}