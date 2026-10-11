package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {

    @Test
    void emptyFieldHasNoOccupiedTiles() {
        GameField field = new GameField(List.of());

        assertFalse(field.isOccupied(new GridPoint2(1, 1)));
    }

    @Test
    void tileWithObstacleIsOccupied() {
        GridPoint2 coordinate = new GridPoint2(1, 3);
        Obstacle obstacle = new Obstacle(coordinate);
        GameField field = new GameField(List.of(obstacle));

        assertTrue(field.isOccupied(coordinate));
    }
}