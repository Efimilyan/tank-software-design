package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldBoundsTest {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;

    private final FieldBounds bounds = new FieldBounds(WIDTH, HEIGHT);

    @Test
    void cornersInsideFieldAreFree() {
        assertFalse(bounds.isOccupied(new GridPoint2(0, 0)));
        assertFalse(bounds.isOccupied(new GridPoint2(WIDTH - 1, HEIGHT - 1)));
    }

    @Test
    void cellInsideFieldIsFree() {
        assertFalse(bounds.isOccupied(new GridPoint2(5, 4)));
    }

    @Test
    void cellsJustOutsideFieldAreBlocked() {
        assertTrue(bounds.isOccupied(new GridPoint2(-1, 0)));
        assertTrue(bounds.isOccupied(new GridPoint2(0, -1)));
        assertTrue(bounds.isOccupied(new GridPoint2(WIDTH, 0)));
        assertTrue(bounds.isOccupied(new GridPoint2(0, HEIGHT)));
    }
}