package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TankControllerTest {
    private static final float SPEED = 0.4f;
    private static final GridPoint2 START = new GridPoint2(1, 1);

    private final Tank tank = new Tank(START, SPEED);

    @Test
    void updateExecutesCommandFromStrategy() {
        TankController controller = new TankController(
                tank, () -> new MoveCommand(Direction.UP), coordinates -> false);

        controller.update(0.1f);

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void updateAsksStrategyEveryTime() {
        int[] calls = {0};
        ControlStrategy counting = () -> {
            calls[0]++;
            return Command.NONE;
        };
        TankController controller = new TankController(tank, counting, coordinates -> false);

        controller.update(0.1f);
        controller.update(0.1f);

        assertEquals(2, calls[0]);
    }

    @Test
    void controllerRespectsCollisionChecker() {
        TankController controller = new TankController(
                tank, () -> new MoveCommand(Direction.UP), coordinates -> true);

        controller.update(0.1f);

        assertEquals(START, tank.getDestinationCoordinates());
    }

    @Test
    void controlledTankStopsInFrontOfObstacle() {
        Obstacle obstacle = new Obstacle(new GridPoint2(1, 3));
        Level level = new Level();
        GameField field = new GameField();
        level.subscribe(field);
        level.add(new TankController(tank, () -> new MoveCommand(Direction.UP), field));
        level.add(tank);
        level.add(obstacle);

        for (int i = 0; i < 20; i++) {
            level.update(0.1f);
        }

        assertEquals(new GridPoint2(1, 2), tank.getCurrentCoordinates());
    }

    @Test
    void controllerDiesTogetherWithItsTank() {
        TankController controller = new TankController(tank, () -> Command.NONE, coordinates -> false);

        tank.takeDamage(Tank.DEFAULT_HEALTH);

        assertFalse(controller.isAlive());
    }
}
