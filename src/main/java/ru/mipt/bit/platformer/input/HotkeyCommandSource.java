package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.GlobalCommand;
import ru.mipt.bit.platformer.control.GlobalCommandSource;

import java.util.ArrayList;
import java.util.List;

/** Адаптер GlobalCommandSource: горячие клавиши. */
public class HotkeyCommandSource implements GlobalCommandSource {
    private final JustPressedKeys keys;
    private final List<GlobalKeyBinding> bindings;

    public HotkeyCommandSource(JustPressedKeys keys, List<GlobalKeyBinding> bindings) {
        this.keys = keys;
        this.bindings = List.copyOf(bindings);
    }

    @Override
    public List<GlobalCommand> pendingCommands() {
        List<GlobalCommand> commands = new ArrayList<>();
        for (GlobalKeyBinding binding : bindings) {
            if (binding.isJustPressed(keys)) {
                commands.add(binding.command());
            }
        }
        return commands;
    }
}