package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Blocking;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.GameWorld;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        List<Blocking> blockers = List.of(obstacle, tank);
        TankController controller = new TankController(
                tank, () -> new MoveCommand(Direction.UP), new GameField(blockers));
        List<GameObject> objects = List.of(controller, tank, obstacle);
        GameWorld world = new GameWorld(objects);

        for (int i = 0; i < 20; i++) {
            world.update(0.1f);
        }

        assertEquals(new GridPoint2(1, 2), tank.getCurrentCoordinates());
    }
}