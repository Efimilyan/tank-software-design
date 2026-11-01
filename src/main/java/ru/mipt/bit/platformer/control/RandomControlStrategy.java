package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;

import java.util.Random;

public class RandomControlStrategy implements ControlStrategy {
    private static final Direction[] DIRECTIONS = Direction.values();

    private final Random random;
    private final double shootProbability;

    public RandomControlStrategy(Random random, double shootProbability) {
        this.random = random;
        this.shootProbability = shootProbability;
    }

    @Override
    public Command nextCommand() {
        if (random.nextDouble() < shootProbability) {
            return new ShootCommand();
        }
        return new MoveCommand(DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
    }
}
