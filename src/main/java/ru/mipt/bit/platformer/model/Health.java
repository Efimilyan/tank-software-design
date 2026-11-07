package ru.mipt.bit.platformer.model;

public class Health {
    private final int max;
    private int current;

    public Health(int max) {
        this.max = max;
        this.current = max;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Урон не может быть отрицательным: " + damage);
        }
        current = Math.max(0, current - damage);
    }

    public boolean isAlive() {
        return current > 0;
    }

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }
}