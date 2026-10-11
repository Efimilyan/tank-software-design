package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Obstacle implements Blocking, GameObject {
    private final GridPoint2 coordinates;

    public Obstacle(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
    }

    @Override
    public boolean occupies(GridPoint2 position) {
        return coordinates.equals(position);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public void update(float deltaTime) {
        // препятствие неподвижно
    }
}