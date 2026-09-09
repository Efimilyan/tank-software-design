package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;


public class Tank {
    private static final float MOVEMENT_SPEED = 0.4f;


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

    public void moveUp(GameField gameField){
        if (isEqual(movementProgress, 1f)) {
            if (!gameField.isOccupied(incrementedY(currentCoordinates))) {
                destinationCoordinates.y++;
                movementProgress = 0f;
            }
            rotation = 90f;
        }

    }
    public void moveDown(GameField gameField){
        if (isEqual(movementProgress, 1f)) {
            if (!gameField.isOccupied(decrementedY(currentCoordinates))) {
                destinationCoordinates.y--;
                movementProgress = 0f;
            }
            rotation = -90f;
        }

    }
    public void moveLeft(GameField gameField){
        if (isEqual(movementProgress, 1f)) {
            if (!gameField.isOccupied(decrementedX(currentCoordinates))) {
                destinationCoordinates.x--;
                movementProgress = 0f;
            }
            rotation = -180f;
        }
    }
    public void moveRight(GameField gameField){
        if (isEqual(movementProgress, 1f)) {
            if (!gameField.isOccupied(incrementedX(currentCoordinates))) {
                destinationCoordinates.x++;
                movementProgress = 0f;
            }
            rotation = 0f;
        }

    }
    public void updateMovementProgress(float deltaTime, TileMovement tileMovement) {
        tileMovement.moveRectangleBetweenTileCenters(boundingRectangle, currentCoordinates, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (isEqual(movementProgress, 1f)) {
            // record that the player has reached his/her destination
            currentCoordinates.set(destinationCoordinates);
        }
    }
    public TextureRegion getGraphics(){
        return graphics;
    }
    public Rectangle getBoundingRectangle(){
        return boundingRectangle;
    }
    public float getRotation(){
        return rotation;
    }
}
