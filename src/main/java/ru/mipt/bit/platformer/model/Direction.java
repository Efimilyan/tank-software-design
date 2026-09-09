package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public enum Direction {
    UP(90f) {
        public GridPoint2 nextCoordinates(GridPoint2 coordinates) {
            return incrementedY(coordinates);
        }
    },
    DOWN(-90f) {
        public GridPoint2 nextCoordinates(GridPoint2 coordinates) {
            return decrementedY(coordinates);
        }
    },
    LEFT(-180f) {
        public GridPoint2 nextCoordinates(GridPoint2 coordinates) {
            return decrementedX(coordinates);
        }
    },
    RIGHT(0f) {
        public GridPoint2 nextCoordinates(GridPoint2 coordinates) {
            return incrementedX(coordinates);
        }
    };

    private final float rotation;

    Direction(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public abstract GridPoint2 nextCoordinates(GridPoint2 coordinates);
}