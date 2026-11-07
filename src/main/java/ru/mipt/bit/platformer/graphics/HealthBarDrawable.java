package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.api.HasHealth;

import java.util.function.Supplier;

public class HealthBarDrawable implements Drawable {
    private final Drawable wrapped;
    private final HealthBar healthBar;
    private final Supplier<Rectangle> bounds;
    private final HasHealth health;

    public HealthBarDrawable(Drawable wrapped, HealthBar healthBar,
                             Supplier<Rectangle> bounds, HasHealth health) {
        this.wrapped = wrapped;
        this.healthBar = healthBar;
        this.bounds = bounds;
        this.health = health;
    }

    @Override
    public void draw(Batch batch) {
        wrapped.draw(batch);
        healthBar.draw(batch, bounds.get(), health.getHealth(), health.getMaxHealth());
    }
}