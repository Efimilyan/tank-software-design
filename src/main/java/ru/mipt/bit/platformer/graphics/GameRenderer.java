package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.graphics.GdxGameUtils.createSingleLayerMapRenderer;

public class GameRenderer implements Disposable {
    private final OwnedResources ownedResources = new OwnedResources();
    private final Batch batch;
    private final MapRenderer levelRenderer;

    public GameRenderer(GameResources resources) {
        batch = ownedResources.own(new SpriteBatch());
        levelRenderer = ownedResources.own(createSingleLayerMapRenderer(resources.getLevel(), batch));
    }

    public void render(Collection<? extends Drawable> drawables) {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        levelRenderer.render();

        batch.begin();
        for (Drawable drawable : drawables) {
            drawable.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        ownedResources.dispose();
    }
}