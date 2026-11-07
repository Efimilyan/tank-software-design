package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.model.BulletType;

public record BulletSettings(float speed, int damage) {
    public static BulletSettings of(BulletType type, int nominalHealth) {
        return new BulletSettings(type.getSpeed(), type.damageFor(nominalHealth));
    }
}