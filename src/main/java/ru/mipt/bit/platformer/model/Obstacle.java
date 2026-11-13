package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.api.Blocking;
import ru.mipt.bit.platformer.model.api.GameObject;
import ru.mipt.bit.platformer.model.api.GridPosition;

public class Obstacle implements Blocking, GameObject {
    private final GridPosition coordinates;

    public Obstacle(GridPosition coordinates) {
        this.coordinates = coordinates;;
    }

    @Override
    public boolean occupies(GridPosition position) {
        return coordinates.equals(position);
    }

    @Override
    public void hit(int damage) {
    }

    public GridPosition getCoordinates() {
        return coordinates;
    }

    @Override
    public void update(float deltaTime) {
        // препятствие неподвижно
    }
}