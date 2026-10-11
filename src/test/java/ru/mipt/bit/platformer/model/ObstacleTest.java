package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleTest {
    private final GridPoint2 coordinate = new GridPoint2(1, 3);

    @Test
    void occupiesItsOwnTile() {
        Obstacle obstacle = new Obstacle(coordinate);

        assertTrue(obstacle.occupies(coordinate));
    }

    @Test
    void doesNotOccupyOtherTile() {
        Obstacle obstacle = new Obstacle(new GridPoint2(1, 2));

        assertFalse(obstacle.occupies(coordinate));
    }
}