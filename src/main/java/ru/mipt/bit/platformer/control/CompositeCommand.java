package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

public class CompositeCommand implements Command {
    private final List<Command> commands;

    public CompositeCommand(List<Command> commands) {
        this.commands = List.copyOf(commands);
    }

    @Override
    public void execute(Tank tank, CollisionChecker collisionChecker) {
        for (Command command : commands) {
            command.execute(tank, collisionChecker);
        }
    }
}
