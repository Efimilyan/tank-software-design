package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileLevelProviderTest {
    @TempDir
    Path dir;

    private LevelData load(String content) throws IOException {
        return load(content, 10, 8);
    }

    private LevelData load(String content, int width, int height) throws IOException {
        Path file = dir.resolve("level.txt");
        Files.writeString(file, content);
        return new FileLevelProvider(file).load(width, height);
    }

    @Test
    void firstLineIsTopRow() throws IOException {
        LevelData level = load("T_\n_X");

        assertEquals(List.of(new TankSpec(new GridPoint2(1, 0), ControlType.HUMAN)), level.tanks());
        assertEquals(List.of(new GridPoint2(0, 1)), level.treePositions());
    }

    @Test
    void aiSymbolCreatesRandomControlledTank() throws IOException {
        LevelData level = load("AX");

        assertEquals(List.of(
                new TankSpec(new GridPoint2(0, 0), ControlType.AI),
                new TankSpec(new GridPoint2(1, 0), ControlType.HUMAN)), level.tanks());
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

    @Test
    void symbolBeyondFieldWidthIsRejected() {
        String line = "_".repeat(10) + "X";

        IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, () -> load(line));

        assertTrue(error.getMessage().contains("Строка 1"));
    }

    @Test
    void tooManyRowsAreRejected() {
        String content = "X\n" + "_\n".repeat(8) + "_";

        assertThrows(IllegalArgumentException.class, () -> load(content));
    }

    @Test
    void levelOfExactlyFieldSizeIsAccepted() throws IOException {
        String content = "X" + "_".repeat(9) + ("\n" + "_".repeat(10)).repeat(7);

        LevelData level = load(content);

        assertEquals(List.of(new TankSpec(new GridPoint2(0, 7), ControlType.HUMAN)), level.tanks());
    }
}
