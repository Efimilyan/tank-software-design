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
        ControlStrategy player = (deltaTime) -> Command.NONE;
        registry.register(ControlType.HUMAN, () -> player);

        assertSame(player, registry.create(ControlType.HUMAN));
    }

    @Test
    void createAsksFactoryEveryTime() {
        int[] calls = {0};
        registry.register(ControlType.AI, () -> {
            calls[0]++;
            return (deltaTime) -> Command.NONE;
        });

        registry.create(ControlType.AI);
        registry.create(ControlType.AI);

        assertEquals(2, calls[0]);
    }

    @Test
    void unregisteredTypeIsRejected() {
        assertThrows(IllegalStateException.class, () -> registry.create(ControlType.AI));
    }
}