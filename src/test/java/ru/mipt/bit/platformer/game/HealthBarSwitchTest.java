package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthBarSwitchTest {
    @Test
    void startsWithGivenState() {
        assertTrue(new HealthBarSwitch(true).isVisible());
        assertFalse(new HealthBarSwitch(false).isVisible());
    }

    @Test
    void toggleTurnsVisibleOff() {
        HealthBarSwitch healthBarSwitch = new HealthBarSwitch(true);

        healthBarSwitch.toggle();

        assertFalse(healthBarSwitch.isVisible());
    }

    @Test
    void secondToggleTurnsVisibleBackOn() {
        HealthBarSwitch healthBarSwitch = new HealthBarSwitch(true);

        healthBarSwitch.toggle();
        healthBarSwitch.toggle();

        assertTrue(healthBarSwitch.isVisible());
    }

    @Test
    void toggleTurnsHiddenOn() {
        HealthBarSwitch healthBarSwitch = new HealthBarSwitch(false);

        healthBarSwitch.toggle();

        assertTrue(healthBarSwitch.isVisible());
    }
}