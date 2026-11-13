package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.api.Blocking;
import ru.mipt.bit.platformer.model.api.Direction;
import ru.mipt.bit.platformer.model.api.GridPosition;
import ru.mipt.bit.platformer.model.api.Gun;

import java.util.ArrayList;
import java.util.List;

public class RecordingGun implements Gun {
    public final List<String> shots = new ArrayList<>();
    public float updated = 0f;

    @Override
    public void update(float deltaTime) {
        updated += deltaTime;
    }

    @Override
    public void fire(GridPosition from, Direction direction, Blocking shooter) {
        shots.add(from + " " + direction);
    }
}
