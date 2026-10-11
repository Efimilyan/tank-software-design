package ru.mipt.bit.platformer.level;

import java.nio.file.Path;
import java.util.Random;

public final class LevelLoaderSwitch {
    private LevelLoaderSwitch() {
    }

    public static LevelLoader select(String[] args) {
        if (args.length == 0 || args[0].equals("default")) {
            return new DefaultLevelLoader();
        }
        if (args[0].equals("random")) {
            return new RandomLevelGenerator(new Random());
        }
        if (args[0].equals("file")) {
            if (args.length < 2) {
                throw new IllegalArgumentException("Нужен путь: file <путь>");
            }
            return new FileLevelLoader(Path.of(args[1]));
        }
        throw new IllegalArgumentException("Режим: default | random | file <путь>");
    }
}