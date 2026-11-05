package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LevelProviderSwitchTest {
    @Test
    void noArgumentsGiveDefaultLevel() {
        assertEquals(DefaultLevelProvider.class, LevelProviderSwitch.select(new String[]{}).getClass());
    }

    @Test
    void defaultKeywordGivesDefaultLevel() {
        assertEquals(DefaultLevelProvider.class,
                LevelProviderSwitch.select(new String[]{"default"}).getClass());
    }

    @Test
    void randomKeywordGivesRandomGenerator() {
        assertEquals(RandomLevelProvider.class,
                LevelProviderSwitch.select(new String[]{"random"}).getClass());
    }

    @Test
    void fileKeywordWithPathGivesFileLoader() {
        assertEquals(FileLevelProvider.class,
                LevelProviderSwitch.select(new String[]{"file", "levels/level1.txt"}).getClass());
    }

    @Test
    void fileKeywordWithoutPathIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> LevelProviderSwitch.select(new String[]{"file"}));
    }

    @Test
    void unknownKeywordIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> LevelProviderSwitch.select(new String[]{"whatever"}));
    }
}