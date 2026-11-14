package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Level;

public class MainCycle {
    private final Controllers controllers;
    private final Level level;
    private final GameView view;

    public MainCycle(Controllers controllers, Level level, GameView view) {
        this.controllers = controllers;
        this.level = level;
        this.view = view;
    }

    public void tick(float deltaTime) {
        controllers.update(deltaTime);
        level.update(deltaTime);
        view.render();
    }
}