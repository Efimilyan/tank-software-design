package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public record LevelData(List<TankSpec> tanks, List<GridPoint2> treePositions) {
    public LevelData {
        long players = tanks.stream()
                .filter(tank -> tank.control() == ControlType.PLAYER)
                .count();
        if (players != 1) {
            throw new IllegalArgumentException(
                    "На уровне должен быть ровно один игрок, найдено: " + players);
        }
        tanks = List.copyOf(tanks);
        treePositions = List.copyOf(treePositions);
    }
}