package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.model.BulletType;

import java.util.OptionalInt;

public record GameSettings(
        float tankSpeed,
        OptionalInt tankHealth,
        BulletType bulletType,
        float reloadTime,
        double enemyShotsPerSecond) {

    public static final int DEFAULT_MIN_TANK_HEALTH = 80;
    public static final int DEFAULT_MAX_TANK_HEALTH = 100;

    public int minTankHealth() {
        return tankHealth.orElse(DEFAULT_MIN_TANK_HEALTH);
    }

    public int maxTankHealth() {
        return tankHealth.orElse(DEFAULT_MAX_TANK_HEALTH);
    }

    public int nominalTankHealth() {
        return maxTankHealth();
    }
}