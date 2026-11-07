package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.BulletType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BulletSettingsTest {
    @Test
    void damageIsCountedFromNominalHealth() {
        assertEquals(25, BulletSettings.of(BulletType.BASE_BULLET, 100).damage());
        assertEquals(13, BulletSettings.of(BulletType.BASE_BULLET, 50).damage());
    }

    @Test
    void speedComesFromBulletType() {
        assertEquals(BulletType.BASE_BULLET.getSpeed(),
                BulletSettings.of(BulletType.BASE_BULLET, 100).speed(), 0.0001f);
    }
}