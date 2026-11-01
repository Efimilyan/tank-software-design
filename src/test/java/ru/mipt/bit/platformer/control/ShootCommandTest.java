package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.RecordingGun;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShootCommandTest {
    @Test
    void commandAsksTankToShoot() {
        RecordingGun gun = new RecordingGun();
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 3, gun);

        new ShootCommand().execute(tank, coordinates -> false);

        assertEquals(List.of("(2, 1) RIGHT"), gun.shots);
    }
}
