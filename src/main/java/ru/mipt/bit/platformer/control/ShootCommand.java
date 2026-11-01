package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Tank;

public class ShootCommand implements Command {
    @Override
    public void execute(Tank tank, CollisionChecker collisionChecker) {
        tank.shoot();
    }
}
