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
    private static final char AI_TANK = 'A';
    private static final char EMPTY = '_';

    private final Path path;

    public FileLevelLoader(Path path) {
        this.path = path;
    }

    @Override
    public LevelData load(int fieldWidth, int fieldHeight) {
        List<String> lines = readLines();
        requireFits(lines, fieldWidth, fieldHeight);
        List<TankSpec> tanks = new ArrayList<>();
        List<GridPoint2> trees = new ArrayList<>();

        for (int row = 0; row < lines.size(); row++) {
            parseRow(lines.get(row), row, lines.size() - 1 - row, tanks, trees);
        }
        return new LevelData(tanks, trees);
    }

    private void parseRow(String line, int row, int y,
                          List<TankSpec> tanks, List<GridPoint2> trees) {
        for (int x = 0; x < line.length(); x++) {
            char symbol = line.charAt(x);
            GridPoint2 position = new GridPoint2(x, y);
            switch (symbol) {
                case TREE -> trees.add(position);
                case PLAYER -> tanks.add(new TankSpec(position, ControlType.PLAYER));
                case AI_TANK -> tanks.add(new TankSpec(position, ControlType.RANDOM));
                case EMPTY -> { }
                default -> throw new IllegalArgumentException(
                        "Символ '" + symbol + "', строка " + (row + 1));
            }
        }
    }

    private void requireFits(List<String> lines, int fieldWidth, int fieldHeight) {
        if (lines.size() > fieldHeight) {
            throw new IllegalArgumentException(
                    "Строк в файле " + lines.size() + ", а высота поля " + fieldHeight);
        }
        for (int row = 0; row < lines.size(); row++) {
            int length = lines.get(row).length();
            if (length > fieldWidth) {
                throw new IllegalArgumentException(
                        "Строка " + (row + 1) + " длиной " + length
                                + " не помещается в поле шириной " + fieldWidth);
            }
        }
    }

    private List<String> readLines() {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Не прочитан " + path, e);
        }
    }
}