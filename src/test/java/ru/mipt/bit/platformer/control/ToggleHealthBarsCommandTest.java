package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToggleHealthBarsCommandTest {
    private int toggles = 0;
    private final ToggleHealthBarsCommand command =
            new ToggleHealthBarsCommand(() -> toggles++);

    @Test
    void executeTogglesTarget() {
        command.execute();

        assertEquals(1, toggles);
    }

    @Test
    void eachExecuteTogglesAgain() {
        command.execute();
        command.execute();

        assertEquals(2, toggles);
    }
}