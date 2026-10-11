package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelLoader {
    private static final double MAX_TREE_FRACTION = 0.25;

    private final Random random;

    public RandomLevelGenerator(Random random) {
        this.random = random;
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

        int maxTreeCount = (int) (cells.size() * MAX_TREE_FRACTION);
        int treeCount = random.nextInt(maxTreeCount + 1);

        GridPoint2 player = cells.get(0);
        List<GridPoint2> trees = new ArrayList<>(cells.subList(1, 1 + treeCount));
        return new LevelData(player, trees);
    }
}