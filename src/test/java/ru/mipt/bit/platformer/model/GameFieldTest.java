package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {

    @Test
    void emptyFieldHasNoOccupiedTiles() {
        GameField field = new GameField(List.of());

        assertFalse(field.isOccupied(new GridPosition(1, 1)));
    }

    @Test
    void tileWithObstacleIsOccupied() {
        GridPosition coordinate = new GridPosition(1, 3);
        Obstacle obstacle = new Obstacle(coordinate);
        GameField field = new GameField(List.of(obstacle));

        assertTrue(field.isOccupied(coordinate));
    }

    @Test
    void tileBecomesOccupiedWhenBlockingObjectIsAddedToLevel() {
        Level level = new Level();
        GameField field = new GameField();
        level.subscribe(field);
        GridPosition coordinate = new GridPosition(2, 2);

        level.add(new Obstacle(coordinate));

        assertTrue(field.isOccupied(coordinate));
    }

    @Test
    void tileBecomesFreeWhenBlockingObjectIsRemovedFromLevel() {
        Level level = new Level();
        GameField field = new GameField();
        level.subscribe(field);
        GridPosition coordinate = new GridPosition(2, 2);
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

        assertFalse(field.isOccupied(new GridPosition(0, 0)));
    }

    private static class Target extends Obstacle {
        int damageTaken = 0;

        Target(GridPosition coordinates) {
            super(coordinates);
        }

        @Override
        public void hit(int damage) {
            damageTaken += damage;
        }
    }

    @Test
    void hitDamagesOnlyObjectsInThatTile() {
        Target inside = new Target(new GridPosition(1, 1));
        Target elsewhere = new Target(new GridPosition(2, 2));
        GameField field = new GameField(List.of(inside, elsewhere));

        field.hit(new GridPosition(1, 1), 3);

        assertEquals(3, inside.damageTaken);
        assertEquals(0, elsewhere.damageTaken);
    }
}
