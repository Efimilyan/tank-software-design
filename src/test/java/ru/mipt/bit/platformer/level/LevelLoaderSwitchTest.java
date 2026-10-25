package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LevelLoaderSwitchTest {
    @Test
    void noArgumentsGiveDefaultLevel() {
        assertEquals(DefaultLevelLoader.class, LevelLoaderSwitch.select(new String[]{}).getClass());
    }

    @Test
    void defaultKeywordGivesDefaultLevel() {
        assertEquals(DefaultLevelLoader.class,
                LevelLoaderSwitch.select(new String[]{"default"}).getClass());
    }

    @Test
    void randomKeywordGivesRandomGenerator() {
        assertEquals(RandomLevelGenerator.class,
                LevelLoaderSwitch.select(new String[]{"random"}).getClass());
    }

    @Test
    void fileKeywordWithPathGivesFileLoader() {
        assertEquals(FileLevelLoader.class,
                LevelLoaderSwitch.select(new String[]{"file", "levels/level1.txt"}).getClass());
    }

    @Test
    void fileKeywordWithoutPathIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> LevelLoaderSwitch.select(new String[]{"file"}));
    }

    @Test
    void unknownKeywordIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> LevelLoaderSwitch.select(new String[]{"whatever"}));
    }
}