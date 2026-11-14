package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.control.RandomControlStrategy;
import ru.mipt.bit.platformer.game.AssembledGame;
import ru.mipt.bit.platformer.game.ControlStrategyRegistry;
import ru.mipt.bit.platformer.game.GameAssembly;
import ru.mipt.bit.platformer.game.GameSettings;
import ru.mipt.bit.platformer.graphics.ResourcePaths;
import ru.mipt.bit.platformer.input.DefaultKeyBindings;
import ru.mipt.bit.platformer.input.KeyboardControlStrategy;
import ru.mipt.bit.platformer.level.ControlType;
import ru.mipt.bit.platformer.level.LevelProviderSwitch;
import ru.mipt.bit.platformer.model.BulletType;

import java.util.OptionalInt;
import java.util.Random;

/** Адаптер: цикл libGDX запускает игру. */
public class GameDesktopLauncher implements ApplicationListener {
    private static final GameSettings SETTINGS = new GameSettings(
            0.4f, OptionalInt.empty(), BulletType.BASE_BULLET, 0.5f, 0.6);
    private static final ResourcePaths RESOURCE_PATHS = new ResourcePaths(
            "level.tmx",
            "images/tank_blue.png",
            "images/greenTree.png",
            "images/bullet.png");

    private final GameAssembly assembly;
    private AssembledGame game;

    public GameDesktopLauncher(GameAssembly assembly) {
        this.assembly = assembly;
    }

    @Override
    public void create() {
        game = assembly.assemble();
    }

    @Override
    public void render() {
        game.mainCycle().tick(Gdx.graphics.getDeltaTime());
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
        game.view().dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        Random random = new Random();
        ControlStrategyRegistry strategies = new ControlStrategyRegistry()
                .register(ControlType.HUMAN, () -> new KeyboardControlStrategy(
                        keyCode -> Gdx.input.isKeyPressed(keyCode),
                        DefaultKeyBindings.movement(),
                        DefaultKeyBindings.actions()))
                .register(ControlType.AI, () -> new RandomControlStrategy(
                        random, SETTINGS.enemyShotsPerSecond()));
        GameAssembly assembly = new GameAssembly(
                LevelProviderSwitch.select(args), SETTINGS, RESOURCE_PATHS, strategies, random,
                keyCode -> Gdx.input.isKeyJustPressed(keyCode));
        new Lwjgl3Application(new GameDesktopLauncher(assembly), config);
    }
}