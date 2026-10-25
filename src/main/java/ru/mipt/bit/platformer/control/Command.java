package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Tank;

@FunctionalInterface
public interface Command {
    Command NONE = (tank, collisionChecker) -> { };

    void execute(Tank tank, CollisionChecker collisionChecker);
}