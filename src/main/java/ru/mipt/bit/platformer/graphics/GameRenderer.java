package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.Disposable;

import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;

public class GameRenderer implements Disposable {
    private final Batch batch;
    private final MapRenderer levelRenderer;

    public GameRenderer(GameResources resources) {
        batch = new SpriteBatch();
        levelRenderer = createSingleLayerMapRenderer(resources.getLevel(), batch);
    }

    public void render(TankDrawable tank, List<ObstacleDrawable> obstacles) {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        levelRenderer.render();

        batch.begin();
        tank.draw(batch);
        for (ObstacleDrawable obstacle : obstacles) {
            obstacle.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}