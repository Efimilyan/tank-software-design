package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.api.CollisionChecker;
import ru.mipt.bit.platformer.model.Tank;

@FunctionalInterface
public interface TankCommand {
    TankCommand NONE = (tank, collisionChecker) -> { };

    void execute(Tank tank, CollisionChecker collisionChecker);
}