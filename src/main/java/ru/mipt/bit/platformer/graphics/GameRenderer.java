package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class GameRenderer implements Disposable {
    private final Batch batch;
    private final MapRenderer levelRenderer;

    public GameRenderer(GameResources resources){
        batch = new SpriteBatch();
        levelRenderer = createSingleLayerMapRenderer(resources.getLevel(), batch);
    }

    public void render(Tank tank, List<Obstacle> obstacles){
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        levelRenderer.render();

        batch.begin();
        drawTextureRegionUnscaled(batch, tank.getGraphics(), tank.getBoundingRectangle(), tank.getRotation());
        for (Obstacle obstacle : obstacles) {
            drawTextureRegionUnscaled(batch, obstacle.getGraphics(), obstacle.getBoundingRectangle(), 0f);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
