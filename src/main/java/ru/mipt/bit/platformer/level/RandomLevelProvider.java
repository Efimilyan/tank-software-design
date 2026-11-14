package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Адаптер LevelProvider: случайный уровень. */
public class RandomLevelProvider implements LevelProvider {
    private static final double MAX_TREE_FRACTION = 0.25;

    private final Random random;
    private final int enemyCount;

    public RandomLevelProvider(Random random, int enemyCount) {
        if (enemyCount < 0) {
            throw new IllegalArgumentException("Число AI-танков не может быть отрицательным");
        }
        this.random = random;
        this.enemyCount = enemyCount;
    }

    @Override
    public LevelData load(int fieldWidth, int fieldHeight) {
        List<GridPosition> cells = shuffledCells(fieldWidth, fieldHeight);
        int treeCount = chooseTreeCount(cells.size());
        List<TankSpec> tanks = placeTanks(cells);
        List<GridPosition> trees = new ArrayList<>(
                cells.subList(1 + enemyCount, 1 + enemyCount + treeCount));
        return new LevelData(tanks, trees);
    }

    private List<GridPosition> shuffledCells(int fieldWidth, int fieldHeight) {
        List<GridPosition> cells = new ArrayList<>();
        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                cells.add(new GridPosition(x, y));
            }
        }
        Collections.shuffle(cells, random);
        return cells;
    }

    private int chooseTreeCount(int cellCount) {
        int cellsLeftForTrees = cellCount - 1 - enemyCount;
        if (cellsLeftForTrees < 0) {
            throw new IllegalArgumentException("Танков больше, чем клеток на поле");
        }
        int maxTreeCount = Math.min((int) (cellCount * MAX_TREE_FRACTION), cellsLeftForTrees);
        return random.nextInt(maxTreeCount + 1);
    }

    private List<TankSpec> placeTanks(List<GridPosition> cells) {
        List<TankSpec> tanks = new ArrayList<>();
        tanks.add(new TankSpec(cells.get(0), ControlType.HUMAN));
        for (int i = 1; i <= enemyCount; i++) {
            tanks.add(new TankSpec(cells.get(i), ControlType.AI));
        }
        return tanks;
    }
}