package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;

import java.util.Random;

public class RandomControlStrategy implements ControlStrategy {
    private static final Direction[] DIRECTIONS = Direction.values();

    private final Random random;

    public RandomControlStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Command nextCommand() {
        return new MoveCommand(DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
    }
}