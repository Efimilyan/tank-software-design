package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalCommandControllerTest {
    private static final float DELTA = 0.1f;

    private final List<String> executed = new ArrayList<>();
    private List<GlobalCommand> pending = List.of();
    private final GlobalCommandController controller =
            new GlobalCommandController(() -> pending);

    @Test
    void executesEveryPendingCommand() {
        pending = List.of(() -> executed.add("first"), () -> executed.add("second"));

        controller.update(DELTA);

        assertEquals(List.of("first", "second"), executed);
    }

    @Test
    void doesNothingWhenNoCommandsArePending() {
        controller.update(DELTA);

        assertTrue(executed.isEmpty());
    }

    @Test
    void asksSourceAgainOnEveryUpdate() {
        pending = List.of(() -> executed.add("first"));
        controller.update(DELTA);
        pending = List.of(() -> executed.add("second"));

        controller.update(DELTA);

        assertEquals(List.of("first", "second"), executed);
    }

    @Test
    void staysAliveWhateverHappens() {
        controller.update(DELTA);

        assertTrue(controller.isAlive());
    }
}