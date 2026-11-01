package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.RecordingGun;
import ru.mipt.bit.platformer.model.Tank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyboardControlStrategyTest {
    private static final float SPEED = 0.4f;
    private static final GridPoint2 START = new GridPoint2(5, 5);

    private final Set<Integer> pressed = new HashSet<>();
    private final KeyboardControlStrategy strategy =
            new KeyboardControlStrategy(keyCode -> pressed.contains(keyCode));

    private GridPoint2 destinationAfterPressing(Integer... keys) {
        pressed.addAll(List.of(keys));
        Tank tank = new Tank(START, SPEED);
        strategy.nextCommand().execute(tank, coordinates -> false);
        return tank.getDestinationCoordinates();
    }

    @Test
    void arrowKeysMoveTank() {
        assertEquals(new GridPoint2(5, 6), destinationAfterPressing(Keys.UP));
        pressed.clear();
        assertEquals(new GridPoint2(5, 4), destinationAfterPressing(Keys.DOWN));
        pressed.clear();
        assertEquals(new GridPoint2(4, 5), destinationAfterPressing(Keys.LEFT));
        pressed.clear();
        assertEquals(new GridPoint2(6, 5), destinationAfterPressing(Keys.RIGHT));
    }

    @Test
    void wasdKeysMoveTank() {
        assertEquals(new GridPoint2(5, 6), destinationAfterPressing(Keys.W));
        pressed.clear();
        assertEquals(new GridPoint2(5, 4), destinationAfterPressing(Keys.S));
        pressed.clear();
        assertEquals(new GridPoint2(4, 5), destinationAfterPressing(Keys.A));
        pressed.clear();
        assertEquals(new GridPoint2(6, 5), destinationAfterPressing(Keys.D));
    }

    @Test
    void noKeysPressedMeansNoMovement() {
        assertEquals(START, destinationAfterPressing());
    }

    @Test
    void firstDirectionByPriorityWinsWhenSeveralKeysPressed() {
        assertEquals(new GridPoint2(5, 6), destinationAfterPressing(Keys.LEFT, Keys.UP));
    }

    @Test
    void spaceMakesTankShoot() {
        pressed.add(Keys.SPACE);
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(START, SPEED, 3, gun);

        strategy.nextCommand().execute(tank, coordinates -> false);

        assertEquals(List.of("(6, 5) RIGHT"), gun.shots);
        assertEquals(START, tank.getDestinationCoordinates());
    }

    @Test
    void directionAndSpaceTogetherTurnMoveAndShootThatWay() {
        pressed.addAll(List.of(Keys.UP, Keys.SPACE));
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(START, SPEED, 3, gun);

        strategy.nextCommand().execute(tank, coordinates -> false);

        assertEquals(new GridPoint2(5, 6), tank.getDestinationCoordinates());
        assertEquals(List.of("(5, 7) UP"), gun.shots);
    }

    @Test
    void noSpaceMeansNoShot() {
        pressed.add(Keys.UP);
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(START, SPEED, 3, gun);

        strategy.nextCommand().execute(tank, coordinates -> false);

        assertEquals(List.of(), gun.shots);
    }
}
