package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

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

    public void render(Tank tank, Obstacle obstacle){
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        // render each tile of the level
        levelRenderer.render();

        // start recording all drawing commands
        batch.begin();
        // render tank
        drawTextureRegionUnscaled(batch, tank.getGraphics(), tank.getBoundingRectangle(), tank.getRotation());
        // render obstacle
        drawTextureRegionUnscaled(batch, obstacle.getGraphics(), obstacle.getBoundingRectangle(), 0f);
        // submit all drawing requests
        batch.end();
    }
    @Override
    public void dispose() {
        batch.dispose();
    }
}
