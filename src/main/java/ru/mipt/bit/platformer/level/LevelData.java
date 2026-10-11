package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public record LevelData(GridPoint2 playerPosition, List<GridPoint2> treePositions) {
}