package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.game.GameWorldFactory;
import ru.mipt.bit.platformer.game.WorldAssembly;
import ru.mipt.bit.platformer.graphics.*;
import ru.mipt.bit.platformer.input.KeyboardControlStrategy;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.LevelLoaderSwitch;
import ru.mipt.bit.platformer.model.GameWorld;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {
    private static final float TANK_SPEED = 0.4f;

    private final LevelLoader levelLoader;
    private final List<Drawable> drawables = new ArrayList<>();
    private GameRenderer renderer;
    private GameResources resources;
    private GameWorld world;
    private final ControlStrategy playerStrategy =
            new KeyboardControlStrategy(keyCode -> Gdx.input.isKeyPressed(keyCode));

    public GameDesktopLauncher(LevelLoader levelLoader) {
        this.levelLoader = levelLoader;
    }

    @Override
    public void create() {
        resources = new GameResources();
        renderer = new GameRenderer(resources);
        TiledMapTileLayer groundLayer = getSingleLayer(resources.getLevel());
        var tankGraphics = new TextureRegion(resources.getBlueTankTexture());
        var treeObstacleGraphics = new TextureRegion(resources.getGreenTreeTexture());

        int fieldWidth = groundLayer.getWidth();
        int fieldHeight = groundLayer.getHeight();
        LevelData level = levelLoader.load(fieldWidth, fieldHeight);

        GameWorldFactory worldFactory = new GameWorldFactory(TANK_SPEED, playerStrategy, new Random());
        WorldAssembly assembly = worldFactory.create(level, fieldWidth, fieldHeight);
        world = assembly.world();

        for (Obstacle obstacle : assembly.obstacles()) {
            drawables.add(new ObstacleDrawable(obstacle, treeObstacleGraphics, groundLayer));
        }
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        for (Tank tank : assembly.tanks()) {
            drawables.add(new TankDrawable(tank, tankGraphics, tileMovement));
        }
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        world.update(deltaTime);
        renderer.render(drawables);
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
        resources.dispose();
        renderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(LevelLoaderSwitch.select(args)), config);
    }
}