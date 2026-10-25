package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.ControlStrategy;
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

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {
    private static final float PLAYER_SPEED = 0.4f;

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
        var playerGraphics = new TextureRegion(resources.getBlueTankTexture());
        var treeObstacleGraphics = new TextureRegion(resources.getGreenTreeTexture());

        LevelData level = levelLoader.load(groundLayer.getWidth(), groundLayer.getHeight());

        Tank tank = new Tank(level.playerPosition(), PLAYER_SPEED);
        List<Obstacle> obstacles = new ArrayList<>();
        for (GridPoint2 position : level.treePositions()) {
            Obstacle obstacle = new Obstacle(position);
            obstacles.add(obstacle);
            drawables.add(new ObstacleDrawable(obstacle, treeObstacleGraphics, groundLayer));
        }
        world = new GameWorld(tank, obstacles);

        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        drawables.add(new TankDrawable(tank, playerGraphics, tileMovement));
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        playerStrategy.nextCommand().execute(world.getTank(), world.getGameField());

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