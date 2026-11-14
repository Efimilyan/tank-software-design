package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DamageStateTest {
    private static final float PRECISION = 0.0001f;

    @Test
    void fullHealthIsLight() {
        assertEquals(DamageState.LIGHT, DamageState.forHealth(1f));
    }

    @Test
    void healthJustAboveSeventyPercentIsLight() {
        assertEquals(DamageState.LIGHT, DamageState.forHealth(0.71f));
    }

    @Test
    void healthOfExactlySeventyPercentIsMedium() {
        assertEquals(DamageState.MEDIUM, DamageState.forHealth(0.70f));
    }

    @Test
    void healthOfExactlyFifteenPercentIsMedium() {
        assertEquals(DamageState.MEDIUM, DamageState.forHealth(0.15f));
    }

    @Test
    void healthBelowFifteenPercentIsHeavy() {
        assertEquals(DamageState.HEAVY, DamageState.forHealth(0.14f));
    }

    @Test
    void lightTankMovesAtNormalSpeedAndShoots() {
        assertEquals(1f, DamageState.LIGHT.movementSpeedFactor(), PRECISION);
        assertTrue(DamageState.LIGHT.canShoot());
    }

    @Test
    void mediumTankMovesTwiceSlowerAndShoots() {
        assertEquals(1f / 2, DamageState.MEDIUM.movementSpeedFactor(), PRECISION);
        assertTrue(DamageState.MEDIUM.canShoot());
    }

    @Test
    void heavyTankMovesThreeTimesSlowerAndCannotShoot() {
        assertEquals(1f / 3, DamageState.HEAVY.movementSpeedFactor(), PRECISION);
        assertFalse(DamageState.HEAVY.canShoot());
    }

    @Test
    void damageDoesNotChangeReloadSpeed() {
        for (DamageState state : DamageState.values()) {
            assertEquals(1f, state.reloadSpeedFactor(), PRECISION);
        }
    }
}