package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.level.ControlType;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ControlStrategyRegistryTest {
    private final ControlStrategyRegistry registry = new ControlStrategyRegistry();

    @Test
    void createReturnsStrategyFromRegisteredFactory() {
        ControlStrategy player = () -> Command.NONE;
        registry.register(ControlType.PLAYER, () -> player);

        assertSame(player, registry.create(ControlType.PLAYER));
    }

    @Test
    void createAsksFactoryEveryTime() {
        int[] calls = {0};
        registry.register(ControlType.RANDOM, () -> {
            calls[0]++;
            return () -> Command.NONE;
        });

        registry.create(ControlType.RANDOM);
        registry.create(ControlType.RANDOM);

        assertEquals(2, calls[0]);
    }

    @Test
    void unregisteredTypeIsRejected() {
        assertThrows(IllegalStateException.class, () -> registry.create(ControlType.RANDOM));
    }
}