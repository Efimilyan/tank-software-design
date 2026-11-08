package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class TankHealthBar implements HealthBar {
    private final TextureRegion pixel;
    private final HealthBarLayout layout;
    private final Color backgroundColor;
    private final Color fillColor;

    public TankHealthBar(TextureRegion pixel, HealthBarLayout layout,
                         Color backgroundColor, Color fillColor) {
        this.pixel = pixel;
        this.layout = layout;
        this.backgroundColor = backgroundColor;
        this.fillColor = fillColor;
    }

    @Override
    public void draw(Batch batch, Rectangle target, int health, int maxHealth) {
        Color previous = new Color(batch.getColor());
        drawRectangle(batch, layout.background(target), backgroundColor);
        drawRectangle(batch, layout.fill(target, health, maxHealth), fillColor);
        batch.setColor(previous);
    }

    private void drawRectangle(Batch batch, Rectangle rectangle, Color color) {
        batch.setColor(color);
        batch.draw(pixel, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}