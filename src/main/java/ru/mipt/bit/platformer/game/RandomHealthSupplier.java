package ru.mipt.bit.platformer.game;

import java.util.Random;
import java.util.function.IntSupplier;

public class RandomHealthSupplier implements IntSupplier {
    private final Random random;
    private final int min;
    private final int max;

    public RandomHealthSupplier(Random random, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Минимум больше максимума: " + min + " > " + max);
        }
        this.random = random;
        this.min = min;
        this.max = max;
    }

    @Override
    public int getAsInt() {
        return min + random.nextInt(max - min + 1);
    }
}