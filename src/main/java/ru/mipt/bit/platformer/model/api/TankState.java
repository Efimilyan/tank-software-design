package ru.mipt.bit.platformer.model.api;

public interface TankState {
    float movementSpeedFactor();

    float reloadSpeedFactor();

    boolean canShoot();
}