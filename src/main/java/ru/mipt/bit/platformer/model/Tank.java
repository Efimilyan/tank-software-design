package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;


public class Tank {

    public enum Direction { UP, DOWN, LEFT, RIGHT };
    private final float movementSpeed;
    private TextureRegion graphics;
    private final Rectangle boundingRectangle;
    private GridPoint2 currentCoordinates;
    private GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private float rotation = 0f;

    public Tank(GridPoint2 coordinates, TextureRegion graphics, float movementSpeed){
        this.currentCoordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.boundingRectangle = createBoundingRectangle(graphics);
        this.graphics = graphics;
        this.movementSpeed = movementSpeed;
    }

    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (isEqual(movementProgress, 1f)) {
            GridPoint2 nextCoordinates = nextCoordinatesFor(direction);
            if (!collisionChecker.isOccupied(nextCoordinates)) {
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
    public void updateMovementProgress(float deltaTime, RectangleMovement rectangleMovement) {
        rectangleMovement.moveRectangleBetweenTileCenters(boundingRectangle, currentCoordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
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
