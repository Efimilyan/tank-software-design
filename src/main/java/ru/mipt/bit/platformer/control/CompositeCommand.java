package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.api.CollisionChecker;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

public class CompositeCommand implements TankCommand {
    private final List<TankCommand> commands;

    public CompositeCommand(List<TankCommand> commands) {
        this.commands = List.copyOf(commands);
    }

    @Override
    public void execute(Tank tank, CollisionChecker collisionChecker) {
        for (TankCommand command : commands) {
            command.execute(tank, collisionChecker);
        }
    }
}
