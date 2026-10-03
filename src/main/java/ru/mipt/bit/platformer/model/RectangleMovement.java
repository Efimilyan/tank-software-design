package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public interface RectangleMovement {
    Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle,
                                              GridPoint2 fromTileCoordinates,
                                              GridPoint2 toTileCoordinates,
                                              float progress);
}