package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.ShootCommand;
import ru.mipt.bit.platformer.model.api.Direction;

import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

public final class DefaultKeyBindings {
    private DefaultKeyBindings() {
    }

    public static List<KeyBinding> movement() {
        return List.of(
                new KeyBinding(new MoveCommand(Direction.UP), UP, W),
                new KeyBinding(new MoveCommand(Direction.DOWN), DOWN, S),
                new KeyBinding(new MoveCommand(Direction.LEFT), LEFT, A),
                new KeyBinding(new MoveCommand(Direction.RIGHT), RIGHT, D));
    }

    public static List<KeyBinding> actions() {
        return List.of(new KeyBinding(new ShootCommand(), SPACE));
    }
}