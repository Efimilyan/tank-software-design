package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TankDrawableTest {
    private final TextureRegion region = new TextureRegion();
    private final TiledMapTileLayer layer = new TiledMapTileLayer(10, 8, 128, 128);

    @Test
    void rectangleFollowsTankToNextTile() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        TankDrawable drawable = new TankDrawable(tank, region, new TileMovement(layer, Interpolation.linear));
        tank.move(Tank.Direction.UP, coordinates -> false);
        tank.update(0.4f);
        tank.update(0.1f);

        drawable.updateRectangle();

        assertEquals(192f, drawable.getRectangle().x, 0.001f);
        assertEquals(320f, drawable.getRectangle().y, 0.001f);
    }
}