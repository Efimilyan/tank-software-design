package ru.mipt.bit.platformer.model;

public enum BulletType {
    BASE_BULLET(0.1f, 4);

    private final float speed;
    private final int hitsToKill;

    BulletType(float speed, int hitsToKill) {
        if (hitsToKill <= 0) {
            throw new IllegalArgumentException("Число попаданий должно быть положительным: " + hitsToKill);
        }
        this.speed = speed;
        this.hitsToKill = hitsToKill;
    }

    public float getSpeed() {
        return speed;
    }

    public int damageFor(int nominalHealth) {
        return (int) Math.ceil((double) nominalHealth / hitsToKill);
    }
}