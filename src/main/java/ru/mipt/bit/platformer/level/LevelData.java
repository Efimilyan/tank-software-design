package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.List;

public record LevelData(List<TankSpec> tanks, List<GridPosition> treePositions) {
    public LevelData {
        long players = tanks.stream()
                .filter(tank -> tank.control() == ControlType.HUMAN)
                .count();
        if (players != 1) {
            throw new IllegalArgumentException(
                    "На уровне должен быть ровно один игрок, найдено: " + players);
        }
        tanks = List.copyOf(tanks);
        treePositions = List.copyOf(treePositions);
    }
}