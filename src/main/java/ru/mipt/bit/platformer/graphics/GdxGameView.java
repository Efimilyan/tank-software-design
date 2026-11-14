package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.control.GameView;

/** Адаптер GameView: рисует через libGDX, владеет ресурсами. */
public class GdxGameView implements GameView, Disposable {
    private final OwnedResources ownedResources = new OwnedResources();
    private final GameRenderer renderer;
    private final LevelView levelView;

    public GdxGameView(GameResources resources, GameRenderer renderer, LevelView levelView) {
        ownedResources.own(resources);
        this.renderer = ownedResources.own(renderer);
        this.levelView = levelView;
    }

    @Override
    public void render() {
        renderer.render(levelView.drawables());
    }

    @Override
    public void dispose() {
        ownedResources.dispose();
    }
}