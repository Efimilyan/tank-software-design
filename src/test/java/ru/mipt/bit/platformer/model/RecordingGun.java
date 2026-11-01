package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

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
    public void fire(GridPoint2 from, Direction direction) {
        shots.add(from + " " + direction);
    }
}
