package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.BulletType;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameSettingsTest {
    private static GameSettings withHealth(OptionalInt health) {
        return new GameSettings(0.4f, health, BulletType.BASE_BULLET, 0.5f, 0.6);
    }

    @Test
    void withoutExplicitHealthRangeIsDefault() {
        GameSettings settings = withHealth(OptionalInt.empty());

        assertEquals(GameSettings.DEFAULT_MIN_TANK_HEALTH, settings.minTankHealth());
        assertEquals(GameSettings.DEFAULT_MAX_TANK_HEALTH, settings.maxTankHealth());
    }

    @Test
    void explicitHealthCollapsesRangeToThatValue() {
        GameSettings settings = withHealth(OptionalInt.of(50));

        assertEquals(50, settings.minTankHealth());
        assertEquals(50, settings.maxTankHealth());
    }

    @Test
    void nominalHealthIsTopOfDefaultRange() {
        assertEquals(GameSettings.DEFAULT_MAX_TANK_HEALTH,
                withHealth(OptionalInt.empty()).nominalTankHealth());
    }

    @Test
    void nominalHealthIsExplicitHealthWhenGiven() {
        assertEquals(50, withHealth(OptionalInt.of(50)).nominalTankHealth());
    }
}