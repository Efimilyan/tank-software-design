package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.api.Direction;
import ru.mipt.bit.platformer.model.api.GridPosition;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveCommandTest {
    private static final float SPEED = 0.4f;
    private static final GridPosition START = new GridPosition(1, 1);

    @Test
    void movesTankInGivenDirection() {
        Tank tank = new Tank(START, SPEED);

        new MoveCommand(Direction.RIGHT).execute(tank, coordinates -> false);

        assertEquals(new GridPosition(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void blockedMoveKeepsTankInPlace() {
        Tank tank = new Tank(START, SPEED);

        new MoveCommand(Direction.RIGHT).execute(tank, coordinates -> true);

        assertEquals(START, tank.getDestinationCoordinates());
    }

    @Test
    void noneCommandDoesNothing() {
        Tank tank = new Tank(START, SPEED);

        TankCommand.NONE.execute(tank, coordinates -> false);

        assertEquals(START, tank.getDestinationCoordinates());
    }
}