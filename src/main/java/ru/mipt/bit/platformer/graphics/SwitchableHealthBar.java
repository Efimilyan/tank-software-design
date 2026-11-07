package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class SwitchableHealthBar implements HealthBar {
    private final HealthBar delegate;
    private final Visibility visibility;

    public SwitchableHealthBar(HealthBar delegate, Visibility visibility) {
        this.delegate = delegate;
        this.visibility = visibility;
    }

    @Override
    public void draw(Batch batch, Rectangle target, int health, int maxHealth) {
        if (visibility.isVisible()) {
            delegate.draw(batch, target, health, maxHealth);
        }
    }
}