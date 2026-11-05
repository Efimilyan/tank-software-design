package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllersTest {
    private static class FakeController implements Controller {
        int updates;
        boolean alive = true;

        @Override
        public void update(float deltaTime) {
            updates++;
        }

        @Override
        public boolean isAlive() {
            return alive;
        }
    }

    private final Controllers controllers = new Controllers();

    @Test
    void updatesEveryAliveController() {
        FakeController first = new FakeController();
        FakeController second = new FakeController();
        controllers.add(first);
        controllers.add(second);

        controllers.update(0.1f);

        assertEquals(1, first.updates);
        assertEquals(1, second.updates);
    }

    @Test
    void deadControllerIsNotUpdatedAndIsRemoved() {
        FakeController dead = new FakeController();
        dead.alive = false;
        controllers.add(dead);

        controllers.update(0.1f);

        assertEquals(0, dead.updates);
        assertEquals(0, controllers.size());
    }

    @Test
    void controllerThatDiesDuringUpdateIsRemovedAtTheEnd() {
        FakeController dying = new FakeController() {
            @Override
            public void update(float deltaTime) {
                super.update(deltaTime);
                alive = false;
            }
        };
        controllers.add(dying);

        controllers.update(0.1f);

        assertEquals(1, dying.updates);
        assertEquals(0, controllers.size());
    }
}