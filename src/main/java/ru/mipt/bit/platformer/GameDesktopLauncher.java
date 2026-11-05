package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.control.RandomControlStrategy;
import ru.mipt.bit.platformer.game.ControlStrategyRegistry;
import ru.mipt.bit.platformer.game.MainCycle;
import ru.mipt.bit.platformer.game.GameAssembly;
import ru.mipt.bit.platformer.game.GameSettings;
import ru.mipt.bit.platformer.graphics.ResourcePaths;
import ru.mipt.bit.platformer.input.DefaultKeyBindings;
import ru.mipt.bit.platformer.input.KeyboardControlStrategy;
import ru.mipt.bit.platformer.level.ControlType;
import ru.mipt.bit.platformer.level.LevelLoaderSwitch;

import java.util.Random;

public class GameDesktopLauncher implements ApplicationListener {
    private static final GameSettings SETTINGS = new GameSettings(
            0.4f, 3, 0.1f, 1, 0.5f, 0.01);
    private static final ResourcePaths RESOURCE_PATHS = new ResourcePaths(
            "level.tmx",
            "images/tank_blue.png",
            "images/greenTree.png",
            "images/bullet.png");

    private final GameAssembly assembly;
    private MainCycle mainCycle;

    public GameDesktopLauncher(GameAssembly assembly) {
        this.assembly = assembly;
    }

    @Override
    public void create() {
        mainCycle = assembly.assemble();
    }

    @Override
    public void render() {
        mainCycle.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        mainCycle.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        Random random = new Random();
        ControlStrategyRegistry strategies = new ControlStrategyRegistry()
                .register(ControlType.PLAYER, () -> new KeyboardControlStrategy(
                        keyCode -> Gdx.input.isKeyPressed(keyCode),
                        DefaultKeyBindings.movement(),
                        DefaultKeyBindings.actions()))
                .register(ControlType.RANDOM, () -> new RandomControlStrategy(
                        random, SETTINGS.enemyShootProbability()));
        GameAssembly assembly = new GameAssembly(
                LevelLoaderSwitch.select(args), SETTINGS, RESOURCE_PATHS, strategies);
        new Lwjgl3Application(new GameDesktopLauncher(assembly), config);
    }
}