package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.utils.Disposable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class OwnedResourcesTest {
    private final List<String> disposed = new ArrayList<>();
    private final OwnedResources resources = new OwnedResources();

    private Disposable resourceNamed(String name) {
        return () -> disposed.add(name);
    }

    @Test
    void ownReturnsTheSameResource() {
        Disposable texture = resourceNamed("texture");

        assertSame(texture, resources.own(texture));
    }

    @Test
    void disposesEveryOwnedResourceInReverseOrderOfCreation() {
        resources.own(resourceNamed("map"));
        resources.own(resourceNamed("tank"));
        resources.own(resourceNamed("tree"));

        resources.dispose();

        assertEquals(List.of("tree", "tank", "map"), disposed);
    }

    @Test
    void eachResourceIsDisposedOnlyOnce() {
        resources.own(resourceNamed("tank"));

        resources.dispose();
        resources.dispose();

        assertEquals(List.of("tank"), disposed);
    }
}