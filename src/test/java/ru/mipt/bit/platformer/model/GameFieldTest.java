package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameFieldTest {
    private final TextureRegion region = new TextureRegion();
    private final TiledMapTileLayer layer = new TiledMapTileLayer(10, 8, 128, 128);

    @Test
    void emptyFieldHasNoOccupiedTiles() {
        GameField field = new GameField(List.of());

        assertFalse(field.isOccupied(new GridPoint2(1, 1)));
    }

    @Test
    void tileWithObstacleIsOccupied() {
        GridPoint2 coordinate = new GridPoint2(1, 3);
        Obstacle obstacle = new Obstacle(coordinate, region, layer);
        GameField field = new GameField(List.of(obstacle));

        assertTrue(field.isOccupied(coordinate));
    }
}