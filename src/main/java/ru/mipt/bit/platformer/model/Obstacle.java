package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Obstacle {
    private TextureRegion graphics;
    private GridPoint2 coordinates;
    private Rectangle rectangle;

    public Obstacle(GridPoint2 coordinates, TextureRegion graphics, TiledMapTileLayer tileLayer){
        this.coordinates = coordinates;
        this.graphics = graphics;
        this.rectangle = createBoundingRectangle(graphics);
        moveRectangleAtTileCenter(tileLayer, rectangle, coordinates);
    }

    public boolean occupies(GridPoint2 position){
        return coordinates.equals(position);
    }

    public TextureRegion getGraphics(){
        return graphics;
    }
    public Rectangle getBoundingRectangle(){
        return rectangle;
    }
}
