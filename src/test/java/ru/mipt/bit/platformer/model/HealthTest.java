package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthTest {
    @Test
    void newHealthIsFull() {
        Health health = new Health(10);

        assertEquals(10, health.getCurrent());
        assertEquals(10, health.getMax());
        assertTrue(health.isAlive());
    }

    @Test
    void damageReducesCurrentButNotMax() {
        Health health = new Health(10);

        health.takeDamage(4);

        assertEquals(6, health.getCurrent());
        assertEquals(10, health.getMax());
    }

    @Test
    void healthDoesNotGoBelowZero() {
        Health health = new Health(3);

        health.takeDamage(10);

        assertEquals(0, health.getCurrent());
        assertFalse(health.isAlive());
    }

    @Test
    void negativeDamageIsRejected() {
        Health health = new Health(3);

        assertThrows(IllegalArgumentException.class, () -> health.takeDamage(-1));
        assertEquals(3, health.getCurrent());
    }
    @Test
    void remainingFractionShowsShareOfMaxHealthLeft() {
        Health health = new Health(10);

        health.takeDamage(3);

        assertEquals(0.7f, health.remainingFraction(), 0.0001f);
    }
}