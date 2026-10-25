package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;

public class TankController implements GameObject {
    private final Tank tank;
    private final ControlStrategy strategy;
    private final CollisionChecker collisionChecker;

    public TankController(Tank tank, ControlStrategy strategy, CollisionChecker collisionChecker) {
        this.tank = tank;
        this.strategy = strategy;
        this.collisionChecker = collisionChecker;
    }

    @Override
    public void update(float deltaTime) {
        strategy.nextCommand().execute(tank, collisionChecker);
    }
}