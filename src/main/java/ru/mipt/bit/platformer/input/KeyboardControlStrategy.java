package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.CompositeCommand;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.TankCommand;

import java.util.ArrayList;
import java.util.List;

/** Адаптер ControlStrategy: клавиатура. */
public class KeyboardControlStrategy implements ControlStrategy {
    private final KeyStateProvider keys;
    private final List<TankKeyBinding> movementBindings;
    private final List<TankKeyBinding> actionBindings;

    public KeyboardControlStrategy(KeyStateProvider keys,
                                   List<TankKeyBinding> movementBindings,
                                   List<TankKeyBinding> actionBindings) {
        this.keys = keys;
        this.movementBindings = List.copyOf(movementBindings);
        this.actionBindings = List.copyOf(actionBindings);
    }

    @Override
    public TankCommand nextCommand(float deltaTime) {
        List<TankCommand> commands = new ArrayList<>();
        for (TankKeyBinding binding : movementBindings) {
            if (binding.isPressed(keys)) {
                commands.add(binding.command());
                break;
            }
        }
        for (TankKeyBinding binding : actionBindings) {
            if (binding.isPressed(keys)) {
                commands.add(binding.command());
            }
        }
        return new CompositeCommand(commands);
    }
}