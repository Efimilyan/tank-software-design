package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class DefaultLevelLoader implements LevelLoader {
    @Override
    public LevelData load(int fieldWidth, int fieldHeight) {
        return new LevelData(
                List.of(new TankSpec(new GridPoint2(1, 1), ControlType.PLAYER),
                        new TankSpec(new GridPoint2(6, 5), ControlType.RANDOM),
                        new TankSpec(new GridPoint2(8, 2), ControlType.RANDOM)),
                List.of(new GridPoint2(1, 3)));
    }
}