package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelLoader {
    private static final double MAX_TREE_FRACTION = 0.25;

    private final Random random;
    private final int enemyCount;

    public RandomLevelGenerator(Random random, int enemyCount) {
        if (enemyCount < 0) {
            throw new IllegalArgumentException("Число AI-танков не может быть отрицательным");
        }
        this.random = random;
        this.enemyCount = enemyCount;
    }

    @Override
    public LevelData load(int fieldWidth, int fieldHeight) {
        List<GridPoint2> cells = new ArrayList<>();
        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(cells, random);

        int cellsLeftForTrees = cells.size() - 1 - enemyCount;
        if (cellsLeftForTrees < 0) {
            throw new IllegalArgumentException("Танков больше, чем клеток на поле");
        }
        int maxTreeCount = Math.min((int) (cells.size() * MAX_TREE_FRACTION), cellsLeftForTrees);
        int treeCount = random.nextInt(maxTreeCount + 1);

        List<TankSpec> tanks = new ArrayList<>();
        tanks.add(new TankSpec(cells.get(0), ControlType.PLAYER));
        for (int i = 1; i <= enemyCount; i++) {
            tanks.add(new TankSpec(cells.get(i), ControlType.RANDOM));
        }
        List<GridPoint2> trees = new ArrayList<>(
                cells.subList(1 + enemyCount, 1 + enemyCount + treeCount));
        return new LevelData(tanks, trees);
    }
}