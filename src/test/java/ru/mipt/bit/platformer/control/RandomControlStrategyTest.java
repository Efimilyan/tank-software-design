package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.RecordingGun;
import ru.mipt.bit.platformer.model.Tank;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomControlStrategyTest {
    private static final float SPEED = 0.4f;
    private static final GridPoint2 START = new GridPoint2(5, 5);
    private static final double NEVER_SHOOTS = 0.0;

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
            RandomControlStrategy strategy = new RandomControlStrategy(alwaysReturning(i), NEVER_SHOOTS);

            assertEquals(directions[i].shift(START), destinationAfter(strategy.nextCommand()));
        }
    }

    @Test
    void allFourDirectionsAreEventuallyChosen() {
        RandomControlStrategy strategy = new RandomControlStrategy(new Random(42), NEVER_SHOOTS);
        Set<GridPoint2> destinations = new HashSet<>();

        for (int i = 0; i < 200; i++) {
            destinations.add(destinationAfter(strategy.nextCommand()));
        }

        assertEquals(4, destinations.size());
    }

    @Test
    void everyCommandMovesTankByExactlyOneTile() {
        RandomControlStrategy strategy = new RandomControlStrategy(new Random(7), NEVER_SHOOTS);

        for (int i = 0; i < 50; i++) {
            GridPoint2 destination = destinationAfter(strategy.nextCommand());
            int distance = Math.abs(destination.x - START.x) + Math.abs(destination.y - START.y);

            assertEquals(1, distance);
        }
    }

    private static Random drawing(double value) {
        return new Random() {
            @Override
            public double nextDouble() {
                return value;
            }
        };
    }

    @Test
    void shootsWhenDrawIsBelowProbability() {
        RandomControlStrategy strategy = new RandomControlStrategy(drawing(0.1), 0.5);
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(START, SPEED, 3, gun);

        strategy.nextCommand().execute(tank, coordinates -> false);

        assertEquals(List.of("(6, 5) RIGHT"), gun.shots);
        assertEquals(START, tank.getDestinationCoordinates());
    }

    @Test
    void movesWhenDrawIsAboveProbability() {
        RandomControlStrategy strategy = new RandomControlStrategy(drawing(0.9), 0.5);
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(START, SPEED, 3, gun);

        strategy.nextCommand().execute(tank, coordinates -> false);

        assertEquals(List.of(), gun.shots);
        assertEquals(1, Math.abs(tank.getDestinationCoordinates().x - START.x)
                + Math.abs(tank.getDestinationCoordinates().y - START.y));
    }
}
