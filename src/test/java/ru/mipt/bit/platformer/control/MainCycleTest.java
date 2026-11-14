package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Level;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainCycleTest {
    private final List<String> calls = new ArrayList<>();
    private final Controllers controllers = new Controllers();
    private final Level level = new Level();
    private final MainCycle mainCycle = new MainCycle(controllers, level, () -> calls.add("view"));

    @Test
    void tickUpdatesControllersThenLevelThenRendersView() {
        controllers.add(new Controller() {
            @Override
            public void update(float deltaTime) {
                calls.add("controllers " + deltaTime);
            }

            @Override
            public boolean isAlive() {
                return true;
            }
        });
        level.add(deltaTime -> calls.add("level " + deltaTime));

        mainCycle.tick(0.5f);

        assertEquals(List.of("controllers 0.5", "level 0.5", "view"), calls);
    }
}