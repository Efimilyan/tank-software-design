package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.CompositeCommand;
import ru.mipt.bit.platformer.control.ControlStrategy;

import java.util.ArrayList;
import java.util.List;

public class KeyboardControlStrategy implements ControlStrategy {
    private final KeyStateProvider keys;
    private final List<KeyBinding> movementBindings;
    private final List<KeyBinding> actionBindings;

    public KeyboardControlStrategy(KeyStateProvider keys,
                                   List<KeyBinding> movementBindings,
                                   List<KeyBinding> actionBindings) {
        this.keys = keys;
        this.movementBindings = List.copyOf(movementBindings);
        this.actionBindings = List.copyOf(actionBindings);
    }

    @Override
    public Command nextCommand(float deltaTime) {
        List<Command> commands = new ArrayList<>();
        for (KeyBinding binding : movementBindings) {
            if (binding.isPressed(keys)) {
                commands.add(binding.command());
                break;
            }
        }
        for (KeyBinding binding : actionBindings) {
            if (binding.isPressed(keys)) {
                commands.add(binding.command());
            }
        }
        return new CompositeCommand(commands);
    }
}