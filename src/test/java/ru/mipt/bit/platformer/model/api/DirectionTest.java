package ru.mipt.bit.platformer.model.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {
    private static final GridPosition ORIGIN = new GridPosition(2, 2);

    @Test
    void shiftMovesByOneTile() {
        assertEquals(new GridPosition(2, 3), Direction.UP.shift(ORIGIN));
        assertEquals(new GridPosition(2, 1), Direction.DOWN.shift(ORIGIN));
        assertEquals(new GridPosition(1, 2), Direction.LEFT.shift(ORIGIN));
        assertEquals(new GridPosition(3, 2), Direction.RIGHT.shift(ORIGIN));
    }

    @Test
    void shiftDoesNotChangeOriginal() {
        Direction.UP.shift(ORIGIN);

        assertEquals(new GridPosition(2, 2), ORIGIN);
    }

    @Test
    void rotationMatchesDirection() {
        assertEquals(90f, Direction.UP.rotation(), 0.001f);
        assertEquals(-90f, Direction.DOWN.rotation(), 0.001f);
        assertEquals(-180f, Direction.LEFT.rotation(), 0.001f);
        assertEquals(0f, Direction.RIGHT.rotation(), 0.001f);
    }
}