package ru.mipt.bit.platformer.level;

import java.nio.file.Path;
import java.util.Random;

public final class LevelProviderSwitch {
    private static final int RANDOM_LEVEL_ENEMY_COUNT = 5;

    private LevelProviderSwitch() {
    }

    public static LevelProvider select(String[] args) {
        if (args.length == 0 || args[0].equals("default")) {
            return new DefaultLevelProvider();
        }
        if (args[0].equals("random")) {
            return new RandomLevelProvider(new Random(), RANDOM_LEVEL_ENEMY_COUNT);
        }
        if (args[0].equals("file")) {
            if (args.length < 2) {
                throw new IllegalArgumentException("Нужен путь: file <путь>");
            }
            return new FileLevelProvider(Path.of(args[1]));
        }
        throw new IllegalArgumentException("Режим: default | random | file <путь>");
    }
}