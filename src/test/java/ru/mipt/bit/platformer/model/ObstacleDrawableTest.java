package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Obstacle;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObstacleDrawableTest {
    private final TextureRegion region = new TextureRegion();
    private final TiledMapTileLayer layer = new TiledMapTileLayer(10, 8, 128, 128);

    @Test
    void rectangleIsPlacedAtTileCenter() {
        ObstacleDrawable drawable = new ObstacleDrawable(new Obstacle(new GridPoint2(1, 3)), region, layer);

        assertEquals(192f, drawable.getRectangle().x, 0.001f);
        assertEquals(448f, drawable.getRectangle().y, 0.001f);
    }
}