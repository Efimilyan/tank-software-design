package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleTest {
    private final TextureRegion region = new TextureRegion();
    private final TiledMapTileLayer layer = new TiledMapTileLayer(10, 8, 128, 128);
    private final GridPoint2 coordinate = new GridPoint2(1, 3);
    @Test
    void occupiesItsOwnTile() {
        Obstacle obstacle = new Obstacle(coordinate, region, layer);

        assertTrue(obstacle.occupies(coordinate));
    }

    @Test
    void doesNotOccupyOtherTile() {
        Obstacle obstacle = new Obstacle(new GridPoint2(1, 2), region, layer);

        assertFalse(obstacle.occupies(coordinate));
    }

    @Test
    void rectangleIsPlacedAtTileCenter() {
        Obstacle obstacle = new Obstacle(coordinate, region, layer);

        assertEquals(192f, obstacle.getBoundingRectangle().x, 0.001f);
        assertEquals(448f, obstacle.getBoundingRectangle().y, 0.001f);
    }
}