package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {
    private static final GridPoint2 ORIGIN = new GridPoint2(2, 2);

    @Test
    void shiftMovesByOneTile() {
        assertEquals(new GridPoint2(2, 3), Direction.UP.shift(ORIGIN));
        assertEquals(new GridPoint2(2, 1), Direction.DOWN.shift(ORIGIN));
        assertEquals(new GridPoint2(1, 2), Direction.LEFT.shift(ORIGIN));
        assertEquals(new GridPoint2(3, 2), Direction.RIGHT.shift(ORIGIN));
    }

    @Test
    void shiftDoesNotChangeOriginal() {
        Direction.UP.shift(ORIGIN);

        assertEquals(new GridPoint2(2, 2), ORIGIN);
    }

    @Test
    void rotationMatchesDirection() {
        assertEquals(90f, Direction.UP.rotation(), 0.001f);
        assertEquals(-90f, Direction.DOWN.rotation(), 0.001f);
        assertEquals(-180f, Direction.LEFT.rotation(), 0.001f);
        assertEquals(0f, Direction.RIGHT.rotation(), 0.001f);
    }
}