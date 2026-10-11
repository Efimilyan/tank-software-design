package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class FixedLevelLoader implements LevelLoader {
    @Override
    public LevelData load() {
        return new LevelData(new GridPoint2(1, 1), List.of(new GridPoint2(1, 3)));
    }
}