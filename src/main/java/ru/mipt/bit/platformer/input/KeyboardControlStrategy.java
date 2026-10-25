package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;

import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

public class KeyboardControlStrategy implements ControlStrategy {
    private static final List<Binding> BINDINGS = List.of(
            new Binding(Direction.UP, UP, W),
            new Binding(Direction.DOWN, DOWN, S),
            new Binding(Direction.LEFT, LEFT, A),
            new Binding(Direction.RIGHT, RIGHT, D));

    private final KeyStateProvider keys;

    public KeyboardControlStrategy(KeyStateProvider keys) {
        this.keys = keys;
    }

    @Override
    public Command nextCommand() {
        for (Binding binding : BINDINGS) {
            if (binding.isPressed(keys)) {
                return new MoveCommand(binding.direction());
            }
        }
        return Command.NONE;
    }

    private record Binding(Direction direction, int... keyCodes) {
        boolean isPressed(KeyStateProvider keys) {
            for (int keyCode : keyCodes) {
                if (keys.isKeyPressed(keyCode)) {
                    return true;
                }
            }
            return false;
        }
    }
}