package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Movable;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class MovableDrawable implements Drawable {
    private final Movable movable;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final RectangleMovement movement;

    public MovableDrawable(Movable movable, TextureRegion graphics, RectangleMovement movement) {
        this.movable = movable;
        this.graphics = graphics;
        this.movement = movement;
        this.rectangle = createBoundingRectangle(graphics);
    }
    @Override
    public void draw(Batch batch) {
        updateRectangle();
        drawTextureRegionUnscaled(batch, graphics, rectangle, movable.getRotation());
    }

    void updateRectangle() {
        movement.moveRectangleBetweenTileCenters(rectangle,
                movable.getCurrentCoordinates(), movable.getDestinationCoordinates(), movable.getMovementProgress());
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}