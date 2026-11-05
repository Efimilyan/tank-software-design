package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleFieldTest {
    private static final GridPoint2 WALL = new GridPoint2(1, 1);

    private final List<String> hits = new ArrayList<>();
    private final BattleField field = BattleField.of(
            coordinates -> coordinates.equals(WALL),
            (coordinates, damage) -> hits.add(coordinates + " " + damage));

    @Test
    void occupiedQuestionGoesToChecker() {
        assertTrue(field.isOccupied(WALL));
        assertFalse(field.isOccupied(new GridPoint2(2, 2)));
    }

    @Test
    void hitGoesToReceiver() {
        field.hit(WALL, 3);

        assertEquals(List.of("(1, 1) 3"), hits);
    }
}