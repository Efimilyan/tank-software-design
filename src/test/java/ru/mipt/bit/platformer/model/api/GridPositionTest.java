package ru.mipt.bit.platformer.model.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GridPositionTest {
    @Test
    void shiftedReturnsNewPositionAndKeepsOriginal() {
        GridPosition origin = new GridPosition(2, 3);

        GridPosition shifted = origin.shifted(1, -1);

        assertEquals(new GridPosition(3, 2), shifted);
        assertEquals(new GridPosition(2, 3), origin);
    }

    @Test
    void positionsWithSameCoordinatesAreEqual() {
        assertEquals(new GridPosition(4, 5), new GridPosition(4, 5));
    }
}