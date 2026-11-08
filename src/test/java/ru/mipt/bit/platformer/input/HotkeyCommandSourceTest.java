package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input.Keys;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.control.GlobalCommand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HotkeyCommandSourceTest {
    private final Set<Integer> justPressed = new HashSet<>();
    private final List<String> executed = new ArrayList<>();
    private final GlobalCommand first = () -> executed.add("first");
    private final GlobalCommand second = () -> executed.add("second");

    private HotkeyCommandSource sourceWith(GlobalKeyBinding... bindings) {
        return new HotkeyCommandSource(justPressed::contains, List.of(bindings));
    }

    private List<String> executePending(HotkeyCommandSource source) {
        executed.clear();
        source.pendingCommands().forEach(GlobalCommand::execute);
        return List.copyOf(executed);
    }

    @Test
    void noCommandsWhenNothingIsPressed() {
        HotkeyCommandSource source = sourceWith(new GlobalKeyBinding(first, Keys.L));

        assertTrue(source.pendingCommands().isEmpty());
    }

    @Test
    void pressedKeyGivesItsCommandOnly() {
        HotkeyCommandSource source = sourceWith(
                new GlobalKeyBinding(first, Keys.L),
                new GlobalKeyBinding(second, Keys.H));
        justPressed.add(Keys.L);

        assertEquals(List.of("first"), executePending(source));
    }

    @Test
    void anyOfBoundKeysTriggersTheCommand() {
        HotkeyCommandSource source = sourceWith(new GlobalKeyBinding(first, Keys.L, Keys.H));
        justPressed.add(Keys.H);

        assertEquals(List.of("first"), executePending(source));
    }

    @Test
    void severalPressedKeysGiveCommandsInBindingOrder() {
        HotkeyCommandSource source = sourceWith(
                new GlobalKeyBinding(first, Keys.L),
                new GlobalKeyBinding(second, Keys.H));
        justPressed.add(Keys.H);
        justPressed.add(Keys.L);

        assertEquals(List.of("first", "second"), executePending(source));
    }

    @Test
    void keysAreReadAgainOnEveryCall() {
        HotkeyCommandSource source = sourceWith(new GlobalKeyBinding(first, Keys.L));
        justPressed.add(Keys.L);
        executePending(source);
        justPressed.clear();

        assertTrue(source.pendingCommands().isEmpty());
    }
}