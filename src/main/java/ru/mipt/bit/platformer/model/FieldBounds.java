package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class FieldBounds implements CollisionChecker {
    private final int width;
    private final int height;

    public FieldBounds(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isOccupied(GridPoint2 coordinates) {
        return coordinates.x < 0 || coordinates.x >= width
                || coordinates.y < 0 || coordinates.y >= height;
    }
}