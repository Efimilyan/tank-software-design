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

    @Test
    void tileBecomesOccupiedWhenBlockingObjectIsAddedToLevel() {
        Level level = new Level();
        GameField field = new GameField();
        level.subscribe(field);
        GridPoint2 coordinate = new GridPoint2(2, 2);

        level.add(new Obstacle(coordinate));

        assertTrue(field.isOccupied(coordinate));
    }

    @Test
    void tileBecomesFreeWhenBlockingObjectIsRemovedFromLevel() {
        Level level = new Level();
        GameField field = new GameField();
        level.subscribe(field);
        GridPoint2 coordinate = new GridPoint2(2, 2);
        Obstacle obstacle = new Obstacle(coordinate);
        level.add(obstacle);

        level.remove(obstacle);

        assertFalse(field.isOccupied(coordinate));
    }

    @Test
    void nonBlockingObjectsDoNotOccupyTiles() {
        Level level = new Level();
        GameField field = new GameField();
        level.subscribe(field);

        level.add(deltaTime -> { });

        assertFalse(field.isOccupied(new GridPoint2(0, 0)));
    }
}
