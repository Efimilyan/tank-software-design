package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.List;

public class DefaultLevelProvider implements LevelProvider {
    @Override
    public LevelData load(int fieldWidth, int fieldHeight) {
        return new LevelData(
                List.of(new TankSpec(new GridPosition(1, 1), ControlType.HUMAN),
                        new TankSpec(new GridPosition(6, 5), ControlType.AI),
                        new TankSpec(new GridPosition(8, 2), ControlType.AI)),
                List.of(new GridPosition(1, 3)));
    }
}