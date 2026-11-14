package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.api.TankState;

public enum DamageState implements TankState {
    LIGHT(1f, 1f, true),
    MEDIUM(1f / 2, 1f, true),
    HEAVY(1f / 3, 1f, false);

    private static final float MEDIUM_DAMAGE_MAX_HEALTH_FRACTION = 0.70f;
    private static final float MEDIUM_DAMAGE_MIN_HEALTH_FRACTION = 0.15f;

    private final float movementSpeedFactor;
    private final float reloadSpeedFactor;
    private final boolean canShoot;

    DamageState(float movementSpeedFactor, float reloadSpeedFactor, boolean canShoot) {
        this.movementSpeedFactor = movementSpeedFactor;
        this.reloadSpeedFactor = reloadSpeedFactor;
        this.canShoot = canShoot;
    }

    public static DamageState forHealth(float healthFraction) {
        if (healthFraction > MEDIUM_DAMAGE_MAX_HEALTH_FRACTION) {
            return LIGHT;
        }
        if (healthFraction >= MEDIUM_DAMAGE_MIN_HEALTH_FRACTION) {
            return MEDIUM;
        }
        return HEAVY;
    }

    @Override
    public float movementSpeedFactor() {
        return movementSpeedFactor;
    }

    @Override
    public float reloadSpeedFactor() {
        return reloadSpeedFactor;
    }

    @Override
    public boolean canShoot() {
        return canShoot;
    }
}