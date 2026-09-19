package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;


public class Tank {
    private static final float MOVEMENT_SPEED = 0.4f;

    public enum Direction { UP, DOWN, LEFT, RIGHT }

    private TextureRegion graphics;
    private final Rectangle boundingRectangle;
    private GridPoint2 currentCoordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(GridPoint2 coordinates, TextureRegion graphics){
        this.currentCoordinates = coordinates;
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.boundingRectangle = createBoundingRectangle(graphics);
        this.graphics = graphics;
    }

    public void move(Direction direction, GameField gameField) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 nextCoordinates = nextCoordinatesFor(direction);
            if (!gameField.isOccupied(nextCoordinates)) {
                destinationCoordinates.set(nextCoordinates);
                movementProgress = 0f;
            }
            rotation = rotationFor(direction);
        }
    }
    private GridPoint2 nextCoordinatesFor(Direction direction) {
        return switch (direction) {
            case UP -> incrementedY(currentCoordinates);
            case DOWN -> decrementedY(currentCoordinates);
            case LEFT -> decrementedX(currentCoordinates);
            case RIGHT -> incrementedX(currentCoordinates);
        };
    }
    private float rotationFor(Direction direction) {
        return switch (direction) {
            case UP -> 90f;
            case DOWN -> -90f;
            case LEFT -> -180f;
            case RIGHT -> 0f;
        };
    }
    public void updateMovementProgress(float deltaTime, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(boundingRectangle, currentCoordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(movementProgress, 1f)) {
            currentCoordinates.set(destinationCoordinates);
        }
    }
    public TextureRegion getGraphics() {
        return graphics;
    }

    public Rectangle getBoundingRectangle() {
        return boundingRectangle;
    }

    public float getRotation() {
        return rotation;
    }
}
