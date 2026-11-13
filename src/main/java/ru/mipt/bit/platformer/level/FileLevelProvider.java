package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.api.GridPosition;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileLevelProvider implements LevelProvider {
    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char AI_TANK = 'A';
    private static final char EMPTY = '_';

    private final Path path;

    public FileLevelProvider(Path path) {
        this.path = path;
    }

    @Override
    public LevelData load(int fieldWidth, int fieldHeight) {
        List<String> lines = readLines();
        requireFits(lines, fieldWidth, fieldHeight);
        List<TankSpec> tanks = new ArrayList<>();
        List<GridPosition> trees = new ArrayList<>();

        for (int row = 0; row < lines.size(); row++) {
            parseRow(lines.get(row), row, lines.size() - 1 - row, tanks, trees);
        }
        return new LevelData(tanks, trees);
    }

    private void parseRow(String line, int row, int y,
                          List<TankSpec> tanks, List<GridPosition> trees) {
        for (int x = 0; x < line.length(); x++) {
            char symbol = line.charAt(x);
            GridPosition position = new GridPosition(x, y);
            switch (symbol) {
                case TREE -> trees.add(position);
                case PLAYER -> tanks.add(new TankSpec(position, ControlType.HUMAN));
                case AI_TANK -> tanks.add(new TankSpec(position, ControlType.AI));
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