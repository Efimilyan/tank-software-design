package ru.mipt.bit.platformer.game;

public record GameSettings(
        float tankSpeed,
        int tankHealth,
        float bulletSpeed,
        int bulletDamage,
        float reloadTime) {
}
