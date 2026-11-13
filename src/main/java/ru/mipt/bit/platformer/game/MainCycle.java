package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.control.Controllers;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.GameResources;
import ru.mipt.bit.platformer.graphics.LevelView;
import ru.mipt.bit.platformer.model.Level;

public class MainCycle implements Disposable {
    private final GameResources resources;
    private final GameRenderer renderer;
    private final LevelView levelView;
    private final Controllers controllers;
    private final Level level;

    public MainCycle(GameResources resources, GameRenderer renderer, LevelView levelView,
                     Controllers controllers, Level level) {
        this.resources = resources;
        this.renderer = renderer;
        this.levelView = levelView;
        this.controllers = controllers;
        this.level = level;
    }

    public void render(float deltaTime) {
        controllers.update(deltaTime);
        level.update(deltaTime);
        renderer.render(levelView.drawables());
    }

    @Override
    public void dispose() {
        renderer.dispose();
        resources.dispose();
    }
}