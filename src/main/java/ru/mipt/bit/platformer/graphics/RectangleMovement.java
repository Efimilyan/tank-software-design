package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.api.GridPosition;

public interface RectangleMovement {
    Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle,
                                              GridPosition fromTileCoordinates,
                                              GridPosition toTileCoordinates,
                                              float progress);
}