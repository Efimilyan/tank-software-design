package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class TankDrawable {
    private final Tank tank;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final RectangleMovement movement;

    public TankDrawable(Tank tank, TextureRegion graphics, RectangleMovement movement) {
        this.tank = tank;
        this.graphics = graphics;
        this.movement = movement;
        this.rectangle = createBoundingRectangle(graphics);
    }

    public void draw(Batch batch) {
        updateRectangle();
        drawTextureRegionUnscaled(batch, graphics, rectangle, tank.getRotation());
    }

    void updateRectangle() {
        movement.moveRectangleBetweenTileCenters(rectangle,
                tank.getCurrentCoordinates(), tank.getDestinationCoordinates(), tank.getMovementProgress());
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}