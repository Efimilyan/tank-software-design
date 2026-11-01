package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.CompositeCommand;
import ru.mipt.bit.platformer.control.Command;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.ShootCommand;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

public class KeyboardControlStrategy implements ControlStrategy {
    private static final List<Binding> BINDINGS = List.of(
            new Binding(Direction.UP, UP, W),
            new Binding(Direction.DOWN, DOWN, S),
            new Binding(Direction.LEFT, LEFT, A),
            new Binding(Direction.RIGHT, RIGHT, D));

    private static final int SHOOT_KEY = SPACE;

    private final KeyStateProvider keys;

    public KeyboardControlStrategy(KeyStateProvider keys) {
        this.keys = keys;
    }

    @Override
    public Command nextCommand() {
        List<Command> commands = new ArrayList<>();
        for (Binding binding : BINDINGS) {
            if (binding.isPressed(keys)) {
                commands.add(new MoveCommand(binding.direction()));
                break;
            }
        }
        if (keys.isKeyPressed(SHOOT_KEY)) {
            commands.add(new ShootCommand());
        }
        return new CompositeCommand(commands);
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