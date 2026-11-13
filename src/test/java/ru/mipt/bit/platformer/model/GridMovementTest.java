package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.GridPosition;

import static org.junit.jupiter.api.Assertions.*;

class GridMovementTest {
    private static final float SPEED = 0.4f;
    private static final GridPosition START = new GridPosition(1, 1);
    private static final GridPosition TARGET = new GridPosition(2, 1);

    private final GridMovement movement = new GridMovement(START, SPEED);

    @Test
    void startsIdleAtGivenPosition() {
        assertTrue(movement.isIdle());
        assertEquals(START, movement.getCurrent());
        assertEquals(START, movement.getDestination());
        assertEquals(1f, movement.getProgress(), 0.001f);
    }

    @Test
    void startMakesMovementBusyAndSetsDestination() {
        movement.start(TARGET);

        assertFalse(movement.isIdle());
        assertEquals(TARGET, movement.getDestination());
        assertEquals(START, movement.getCurrent());
        assertEquals(0f, movement.getProgress(), 0.001f);
    }

    @Test
    void updateAdvancesProgressBySpeed() {
        movement.start(TARGET);

        movement.update(0.2f);

        assertEquals(0.5f, movement.getProgress(), 0.001f);
        assertEquals(START, movement.getCurrent());
    }

    @Test
    void arrivalMovesCurrentToDestination() {
        movement.start(TARGET);

        movement.update(SPEED);

        assertTrue(movement.isIdle());
        assertEquals(TARGET, movement.getCurrent());
    }

    @Test
    void progressIsClampedToOne() {
        movement.start(TARGET);

        movement.update(10f);

        assertEquals(1f, movement.getProgress(), 0.001f);
    }

    @Test
    void occupiesBothCurrentAndDestinationWhileMoving() {
        movement.start(TARGET);

        assertTrue(movement.occupies(START));
        assertTrue(movement.occupies(TARGET));
        assertFalse(movement.occupies(new GridPosition(3, 1)));
    }

    @Test
    void doesNotChangePassedStartPoint() {
        movement.start(TARGET);
        movement.update(SPEED);

        assertEquals(new GridPosition(1, 1), START);
    }
}