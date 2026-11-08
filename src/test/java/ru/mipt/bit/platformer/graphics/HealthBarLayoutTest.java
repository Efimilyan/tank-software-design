package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthBarLayoutTest {
    private static final float BAR_HEIGHT = 8f;
    private static final float GAP = 4f;
    private static final float DELTA = 0.001f;

    private final HealthBarLayout layout = new HealthBarLayout(BAR_HEIGHT, GAP);
    private final Rectangle target = new Rectangle(100f, 200f, 128f, 128f);

    @Test
    void backgroundIsAboveTargetAndAsWideAsTarget() {
        Rectangle background = layout.background(target);

        assertEquals(100f, background.x, DELTA);
        assertEquals(332f, background.y, DELTA);
        assertEquals(128f, background.width, DELTA);
        assertEquals(BAR_HEIGHT, background.height, DELTA);
    }

    @Test
    void fillWidthIsProportionalToHealth() {
        Rectangle fill = layout.fill(target, 50, 100);

        assertEquals(64f, fill.width, DELTA);
    }

    @Test
    void fillSharesPositionAndHeightWithBackground() {
        Rectangle fill = layout.fill(target, 50, 100);

        assertEquals(100f, fill.x, DELTA);
        assertEquals(332f, fill.y, DELTA);
        assertEquals(BAR_HEIGHT, fill.height, DELTA);
    }

    @Test
    void fullHealthFillsWholeBackground() {
        assertEquals(128f, layout.fill(target, 100, 100).width, DELTA);
    }

    @Test
    void zeroHealthGivesEmptyFill() {
        assertEquals(0f, layout.fill(target, 0, 100).width, DELTA);
    }

    @Test
    void healthAboveMaxDoesNotOverflowBackground() {
        assertEquals(128f, layout.fill(target, 150, 100).width, DELTA);
    }

    @Test
    void negativeHealthGivesEmptyFill() {
        assertEquals(0f, layout.fill(target, -10, 100).width, DELTA);
    }

    @Test
    void zeroMaxHealthGivesEmptyFill() {
        assertEquals(0f, layout.fill(target, 5, 0).width, DELTA);
    }

    @Test
    void layoutDoesNotChangeTarget() {
        layout.fill(target, 50, 100);

        assertEquals(new Rectangle(100f, 200f, 128f, 128f), target);
    }
}