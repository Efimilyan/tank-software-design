package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.api.HasHealth;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthBarDrawableTest {
    private final List<String> calls = new ArrayList<>();
    private final Rectangle rectangle = new Rectangle(1f, 2f, 3f, 4f);
    private final int[] currentHealth = {70};
    private final HasHealth health = new HasHealth() {
        @Override
        public int getHealth() {
            return currentHealth[0];
        }

        @Override
        public int getMaxHealth() {
            return 100;
        }
    };
    private final HealthBar healthBar = (batch, target, current, max) ->
            calls.add("bar " + target.x + " " + current + " " + max);

    @Test
    void drawsWrappedObjectBeforeHealthBar() {
        Drawable wrapped = batch -> calls.add("wrapped");
        HealthBarDrawable drawable = new HealthBarDrawable(wrapped, healthBar, () -> rectangle, health);

        drawable.draw(null);

        assertEquals(List.of("wrapped", "bar 1.0 70 100"), calls);
    }

    @Test
    void healthBarGetsRectangleAsUpdatedByWrappedObject() {
        Drawable wrapped = batch -> rectangle.x = 50f;
        HealthBarDrawable drawable = new HealthBarDrawable(wrapped, healthBar, () -> rectangle, health);

        drawable.draw(null);

        assertEquals(List.of("bar 50.0 70 100"), calls);
    }

    @Test
    void healthIsReadOnEveryDraw() {
        HealthBarDrawable drawable = new HealthBarDrawable(batch -> { }, healthBar, () -> rectangle, health);

        drawable.draw(null);
        currentHealth[0] = 30;
        drawable.draw(null);

        assertEquals(List.of("bar 1.0 70 100", "bar 1.0 30 100"), calls);
    }
}