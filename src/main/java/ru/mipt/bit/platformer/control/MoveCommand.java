package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.api.CollisionChecker;
import ru.mipt.bit.platformer.model.api.Direction;
import ru.mipt.bit.platformer.model.Tank;

public class MoveCommand implements Command {
    private final Direction direction;

    public MoveCommand(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void execute(Tank tank, CollisionChecker collisionChecker) {
        tank.move(direction, collisionChecker);
    }
}