package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {
    private final List<String> events = new ArrayList<>();
    private final Level level = new Level();

    private final GameObject first = delta -> events.add("tick first");
    private final GameObject second = delta -> events.add("tick second");

    private void recordEvents() {
        level.subscribe(new LevelListener() {
            @Override
            public void objectAdded(GameObject object) {
                events.add("added " + (object == first ? "first" : "second"));
            }

            @Override
            public void objectRemoved(GameObject object) {
                events.add("removed " + (object == first ? "first" : "second"));
            }
        });
    }

    @Test
    void notifiesSubscribersAboutAddedObject() {
        recordEvents();

        level.add(first);

        assertEquals(List.of("added first"), events);
        assertEquals(List.of(first), level.objects());
    }

    @Test
    void notifiesSubscribersAboutRemovedObject() {
        level.add(first);
        recordEvents();

        level.remove(first);

        assertEquals(List.of("removed first"), events);
        assertTrue(level.objects().isEmpty());
    }

    @Test
    void ignoresDuplicateAddAndUnknownRemove() {
        level.add(first);
        recordEvents();

        level.add(first);
        level.remove(second);

        assertTrue(events.isEmpty());
        assertEquals(List.of(first), level.objects());
    }

    @Test
    void updatesObjectsInInsertionOrder() {
        level.add(first);
        level.add(second);

        level.update(0.1f);

        assertEquals(List.of("tick first", "tick second"), events);
    }

    @Test
    void objectAddedDuringUpdateStartsTickingNextUpdate() {
        GameObject spawner = delta -> level.add(second);
        level.add(spawner);

        level.update(0.1f);
        assertTrue(events.isEmpty());

        level.update(0.1f);
        assertEquals(List.of("tick second"), events);
    }

    @Test
    void objectRemovedDuringUpdateIsNotUpdatedAnymore() {
        GameObject killer = delta -> level.remove(second);
        level.add(killer);
        level.add(second);

        level.update(0.1f);

        assertTrue(events.isEmpty());
        assertEquals(List.of(killer), level.objects());
    }
}
