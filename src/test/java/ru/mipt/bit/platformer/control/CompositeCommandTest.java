package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.RecordingGun;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.api.Direction;
import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompositeCommandTest {
    @Test
    void executesAllCommandsInOrder() {
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f, 3, gun);
        TankCommand command = new CompositeCommand(
                List.of(new MoveCommand(Direction.UP), new ShootCommand()));

        command.execute(tank, coordinates -> false);

        assertEquals(new GridPosition(1, 2), tank.getDestinationCoordinates());
        assertEquals(List.of("(1, 2) UP"), gun.shots);
    }

    @Test
    void emptyCompositeDoesNothing() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);

        new CompositeCommand(List.of()).execute(tank, coordinates -> false);

        assertEquals(new GridPosition(1, 1), tank.getDestinationCoordinates());
    }
}
