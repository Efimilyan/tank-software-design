package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Obstacle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class ObstacleDrawable implements Drawable {
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    public ObstacleDrawable(Obstacle obstacle, TextureRegion graphics, TiledMapTileLayer tileLayer) {
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(tileLayer, rectangle, obstacle.getCoordinates());
    }
    @Override
    public void draw(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, 0f);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}