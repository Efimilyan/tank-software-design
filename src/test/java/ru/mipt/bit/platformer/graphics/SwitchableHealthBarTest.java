package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SwitchableHealthBarTest {
    private final List<String> calls = new ArrayList<>();
    private final Rectangle target = new Rectangle(1f, 2f, 3f, 4f);
    private final HealthBar delegate = (batch, rectangle, health, maxHealth) ->
            calls.add(rectangle.x + " " + health + " " + maxHealth);
    private boolean visible = true;
    private final SwitchableHealthBar bar = new SwitchableHealthBar(delegate, () -> visible);

    @Test
    void drawsThroughWhenVisible() {
        bar.draw(null, target, 70, 100);

        assertEquals(List.of("1.0 70 100"), calls);
    }

    @Test
    void doesNotDrawWhenHidden() {
        visible = false;

        bar.draw(null, target, 70, 100);

        assertTrue(calls.isEmpty());
    }

    @Test
    void followsVisibilityChangesBetweenCalls() {
        bar.draw(null, target, 70, 100);
        visible = false;
        bar.draw(null, target, 60, 100);
        visible = true;
        bar.draw(null, target, 50, 100);

        assertEquals(List.of("1.0 70 100", "1.0 50 100"), calls);
    }
}