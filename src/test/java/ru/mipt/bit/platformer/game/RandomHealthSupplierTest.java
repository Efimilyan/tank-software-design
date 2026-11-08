package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RandomHealthSupplierTest {
    private static Random fixed(int value) {
        return new Random() {
            @Override
            public int nextInt(int bound) {
                return Math.min(value, bound - 1);
            }
        };
    }

    @Test
    void lowestRandomValueGivesMin() {
        assertEquals(80, new RandomHealthSupplier(fixed(0), 80, 100).getAsInt());
    }

    @Test
    void highestRandomValueGivesMax() {
        assertEquals(100, new RandomHealthSupplier(fixed(Integer.MAX_VALUE), 80, 100).getAsInt());
    }

    @Test
    void equalBoundsGiveThatValue() {
        assertEquals(50, new RandomHealthSupplier(fixed(7), 50, 50).getAsInt());
    }

    @Test
    void minAboveMaxIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomHealthSupplier(new Random(), 10, 5));
    }
}