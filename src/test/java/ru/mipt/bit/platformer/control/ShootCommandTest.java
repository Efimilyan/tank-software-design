package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Gun;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShootCommandTest {
    @Test
    void commandAsksTankToShoot() {
        List<String> shots = new ArrayList<>();
        Gun recordingGun = new Gun() {
            @Override
            public void update(float deltaTime) {
            }

            @Override
            public void fire(GridPoint2 from, Direction direction) {
                shots.add(from + " " + direction);
            }
        };
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 3, recordingGun);

        new ShootCommand().execute(tank, coordinates -> false);

        assertEquals(List.of("(2, 1) RIGHT"), shots);
    }
}
