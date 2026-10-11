package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileLevelLoader implements LevelLoader {
    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private final Path path;

    public FileLevelLoader(Path path) {
        this.path = path;
    }

    @Override
    public LevelData load() {
        List<String> lines = readLines();
        GridPoint2 player = null;
        List<GridPoint2> trees = new ArrayList<>();

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            int y = lines.size() - 1 - row;
            for (int x = 0; x < line.length(); x++) {
                char symbol = line.charAt(x);
                switch (symbol) {
                    case TREE -> trees.add(new GridPoint2(x, y));
                    case PLAYER -> {
                        if (player != null) {
                            throw new IllegalArgumentException("В уровне больше одного игрока (X)");
                        }
                        player = new GridPoint2(x, y);
                    }
                    case EMPTY -> { }
                    default -> throw new IllegalArgumentException(
                            "Неизвестный символ '" + symbol + "' в строке " + (row + 1));
                }
            }
        }
        if (player == null) {
            throw new IllegalArgumentException("В уровне нет игрока (X)");
        }
        return new LevelData(player, trees);
    }

    private List<String> readLines() {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось прочитать уровень " + path, e);
        }
    }
}