package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.api.Direction;

import java.util.Random;

public class RandomControlStrategy implements ControlStrategy {
    private static final Direction[] DIRECTIONS = Direction.values();

    private final Random random;
    private final double shotsPerSecond;

    public RandomControlStrategy(Random random, double shotsPerSecond) {
        this.random = random;
        this.shotsPerSecond = shotsPerSecond;
    }

    @Override
    public TankCommand nextCommand(float deltaTime) {
        if (random.nextDouble() < shotsPerSecond * deltaTime) {
            return new ShootCommand();
        }
        return new MoveCommand(DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
    }
}