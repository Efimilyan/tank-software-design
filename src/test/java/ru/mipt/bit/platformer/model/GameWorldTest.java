package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameWorldTest {
    @Test
    void updatesEveryObjectWithSameDeltaInOrder() {
        List<String> log = new ArrayList<>();
        GameObject first = delta -> log.add("first " + delta);
        GameObject second = delta -> log.add("second " + delta);
        GameWorld world = new GameWorld(List.of(first, second));

        world.update(0.5f);

        assertEquals(List.of("first 0.5", "second 0.5"), log);
    }

    @Test
    void updateEachTimeReachesObjects() {
        int[] updates = {0};
        GameObject counter = delta -> updates[0]++;
        GameWorld world = new GameWorld(List.of(counter));

        world.update(0.1f);
        world.update(0.1f);

        assertEquals(2, updates[0]);
    }

    @Test
    void emptyWorldCanBeUpdated() {
        new GameWorld(List.of()).update(0.1f);
    }
}