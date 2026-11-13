package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.Blocking;
import ru.mipt.bit.platformer.model.api.CollisionChecker;
import ru.mipt.bit.platformer.model.api.Direction;
import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeCollisionCheckerTest {
    private static final float SPEED = 0.4f;

    private final Obstacle tree = new Obstacle(new GridPosition(2, 2));
    private final Tank tank = new Tank(new GridPosition(4, 4), SPEED);
    private final CollisionChecker checker = createChecker();

    private CollisionChecker createChecker() {
        List<Blocking> blockers = List.of(tree, tank);
        return new CompositeCollisionChecker(List.of(
                new FieldBounds(10, 8),
                new GameField(blockers)));
    }

    @Test
    void outsideOfFieldIsBlocked() {
        assertTrue(checker.isOccupied(new GridPosition(-1, 3)));
        assertTrue(checker.isOccupied(new GridPosition(10, 3)));
    }

    @Test
    void treeTileIsBlocked() {
        assertTrue(checker.isOccupied(new GridPosition(2, 2)));
    }

    @Test
    void idleTankTileIsBlocked() {
        assertTrue(checker.isOccupied(new GridPosition(4, 4)));
    }

    @Test
    void movingTankBlocksBothTiles() {
        tank.move(Direction.UP, coordinates -> false);

        assertTrue(checker.isOccupied(new GridPosition(4, 4)));
        assertTrue(checker.isOccupied(new GridPosition(4, 5)));
    }

    @Test
    void freeTileInsideFieldIsNotBlocked() {
        assertFalse(checker.isOccupied(new GridPosition(6, 6)));
    }

    @Test
    void emptyCompositeBlocksNothing() {
        CollisionChecker empty = new CompositeCollisionChecker(List.of());

        assertFalse(empty.isOccupied(new GridPosition(-100, 100)));
    }
}
