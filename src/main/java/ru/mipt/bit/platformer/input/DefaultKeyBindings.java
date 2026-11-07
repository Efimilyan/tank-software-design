package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.MoveCommand;
import ru.mipt.bit.platformer.control.ShootCommand;
import ru.mipt.bit.platformer.model.api.Direction;

import java.util.List;

import static com.badlogic.gdx.Input.Keys.*;

public final class DefaultKeyBindings {
    private DefaultKeyBindings() {
    }

    public static List<TankKeyBinding> movement() {
        return List.of(
                new TankKeyBinding(new MoveCommand(Direction.UP), UP, W),
                new TankKeyBinding(new MoveCommand(Direction.DOWN), DOWN, S),
                new TankKeyBinding(new MoveCommand(Direction.LEFT), LEFT, A),
                new TankKeyBinding(new MoveCommand(Direction.RIGHT), RIGHT, D));
    }

    public static List<TankKeyBinding> actions() {
        return List.of(new TankKeyBinding(new ShootCommand(), SPACE));
    }
}