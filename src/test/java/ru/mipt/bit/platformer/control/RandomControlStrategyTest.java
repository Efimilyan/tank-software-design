package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomControlStrategyTest {
    private static final float SPEED = 0.4f;
    private static final GridPoint2 START = new GridPoint2(5, 5);

    private static Random alwaysReturning(int value) {
        return new Random() {
            @Override
            public int nextInt(int bound) {
                return value;
            }
        };
    }

    private static GridPoint2 destinationAfter(Command command) {
        Tank tank = new Tank(START, SPEED);
        command.execute(tank, coordinates -> false);
        return tank.getDestinationCoordinates();
    }

    @Test
    void eachRandomValueMapsToItsOwnDirection() {
        Direction[] directions = Direction.values();

        for (int i = 0; i < directions.length; i++) {
            RandomControlStrategy strategy = new RandomControlStrategy(alwaysReturning(i));

            assertEquals(directions[i].shift(START), destinationAfter(strategy.nextCommand()));
        }
    }

    @Test
    void allFourDirectionsAreEventuallyChosen() {
        RandomControlStrategy strategy = new RandomControlStrategy(new Random(42));
        Set<GridPoint2> destinations = new HashSet<>();

        for (int i = 0; i < 200; i++) {
            destinations.add(destinationAfter(strategy.nextCommand()));
        }

        assertEquals(4, destinations.size());
    }

    @Test
    void everyCommandMovesTankByExactlyOneTile() {
        RandomControlStrategy strategy = new RandomControlStrategy(new Random(7));

        for (int i = 0; i < 50; i++) {
            GridPoint2 destination = destinationAfter(strategy.nextCommand());
            int distance = Math.abs(destination.x - START.x) + Math.abs(destination.y - START.y);

            assertEquals(1, distance);
        }
    }
}