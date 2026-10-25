package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
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
}