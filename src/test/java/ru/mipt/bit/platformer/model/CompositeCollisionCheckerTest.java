package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeCollisionCheckerTest {
    private static final float SPEED = 0.4f;

    private final Obstacle tree = new Obstacle(new GridPoint2(2, 2));
    private final Tank tank = new Tank(new GridPoint2(4, 4), SPEED);
    private final CollisionChecker checker = createChecker();

    private CollisionChecker createChecker() {
        List<Blocking> blockers = List.of(tree, tank);
        return new CompositeCollisionChecker(List.of(
                new FieldBounds(10, 8),
                new GameField(blockers)));
    }

    @Test
    void outsideOfFieldIsBlocked() {
        assertTrue(checker.isOccupied(new GridPoint2(-1, 3)));
        assertTrue(checker.isOccupied(new GridPoint2(10, 3)));
    }

    @Test
    void treeTileIsBlocked() {
        assertTrue(checker.isOccupied(new GridPoint2(2, 2)));
    }

    @Test
    void idleTankTileIsBlocked() {
        assertTrue(checker.isOccupied(new GridPoint2(4, 4)));
    }

    @Test
    void movingTankBlocksBothTiles() {
        tank.move(Direction.UP, coordinates -> false);

        assertTrue(checker.isOccupied(new GridPoint2(4, 4)));
        assertTrue(checker.isOccupied(new GridPoint2(4, 5)));
    }

    @Test
    void freeTileInsideFieldIsNotBlocked() {
        assertFalse(checker.isOccupied(new GridPoint2(6, 6)));
    }

    @Test
    void emptyCompositeBlocksNothing() {
        CollisionChecker empty = new CompositeCollisionChecker(List.of());

        assertFalse(empty.isOccupied(new GridPoint2(-100, 100)));
    }

    @Test
    void hitIsPassedToEveryChecker() {
        int[] damage = {0, 0};
        CollisionChecker first = new CollisionChecker() {
            @Override
            public boolean isOccupied(GridPoint2 coordinates) {
                return false;
            }

            @Override
            public void hit(GridPoint2 coordinates, int amount) {
                damage[0] += amount;
            }
        };
        CollisionChecker second = new CollisionChecker() {
            @Override
            public boolean isOccupied(GridPoint2 coordinates) {
                return false;
            }

            @Override
            public void hit(GridPoint2 coordinates, int amount) {
                damage[1] += amount;
            }
        };

        new CompositeCollisionChecker(List.of(first, second)).hit(new GridPoint2(1, 1), 2);

        assertEquals(2, damage[0]);
        assertEquals(2, damage[1]);
    }
}
