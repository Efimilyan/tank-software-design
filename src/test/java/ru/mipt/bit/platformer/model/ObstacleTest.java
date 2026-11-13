package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.GridPosition;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObstacleTest {
    private final GridPosition coordinate = new GridPosition(1, 3);

    @Test
    void occupiesItsOwnTile() {
        Obstacle obstacle = new Obstacle(coordinate);

        assertTrue(obstacle.occupies(coordinate));
    }

    @Test
    void doesNotOccupyOtherTile() {
        Obstacle obstacle = new Obstacle(new GridPosition(1, 2));

        assertFalse(obstacle.occupies(coordinate));
    }
}