package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;

public class HealthBarLayout {
    private final float height;
    private final float gap;

    public HealthBarLayout(float height, float gap) {
        this.height = height;
        this.gap = gap;
    }

    public Rectangle background(Rectangle target) {
        return new Rectangle(target.x, target.y + target.height + gap, target.width, height);
    }

    public Rectangle fill(Rectangle target, int health, int maxHealth) {
        Rectangle bar = background(target);
        bar.width = bar.width * fraction(health, maxHealth);
        return bar;
    }

    private float fraction(int health, int maxHealth) {
        if (maxHealth <= 0) {
            return 0f;
        }
        return Math.clamp((float) health / maxHealth, 0f, 1f);
    }
}