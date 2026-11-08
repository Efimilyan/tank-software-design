package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public interface HealthBar {
    void draw(Batch batch, Rectangle target, int health, int maxHealth);
}