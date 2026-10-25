package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileLevelLoaderTest {
    @TempDir
    Path dir;

    private LevelData load(String content) throws IOException {
        Path file = dir.resolve("level.txt");
        Files.writeString(file, content);
        return new FileLevelLoader(file).load(10, 8);
    }

    @Test
    void firstLineIsTopRow() throws IOException {
        LevelData level = load("T_\n_X");

        assertEquals(List.of(new TankSpec(new GridPoint2(1, 0), ControlType.PLAYER)), level.tanks());
        assertEquals(List.of(new GridPoint2(0, 1)), level.treePositions());
    }

    @Test
    void aiSymbolCreatesRandomControlledTank() throws IOException {
        LevelData level = load("AX");

        assertEquals(List.of(
                new TankSpec(new GridPoint2(0, 0), ControlType.RANDOM),
                new TankSpec(new GridPoint2(1, 0), ControlType.PLAYER)), level.tanks());
    }

    @Test
    void levelWithoutPlayerIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> load("T_\n__"));
    }

    @Test
    void levelWithTwoPlayersIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> load("X_\n_X"));
    }

    @Test
    void unknownSymbolIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> load("X?"));
    }
}