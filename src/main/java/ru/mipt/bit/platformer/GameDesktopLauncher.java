package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.GameResources;
import ru.mipt.bit.platformer.graphics.ObstacleDrawable;
import ru.mipt.bit.platformer.input.KeyStateProvider;
import ru.mipt.bit.platformer.input.PlayerKeyboardInput;
import ru.mipt.bit.platformer.model.GameWorld;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {
    private static final GridPoint2 PLAYER_START = new GridPoint2(1, 1);
    private static final float PLAYER_SPEED = 0.4f;
    private static final List<GridPoint2> TREE_POSITIONS = List.of(new GridPoint2(1, 3));
    private final List<ObstacleDrawable> obstacleDrawables = new ArrayList<>();

    private GameRenderer renderer;
    private GameResources resources;
    private GameWorld world;
    private final PlayerKeyboardInput playerInput =
            new PlayerKeyboardInput(new KeyStateProvider() {
                @Override
                public boolean isKeyPressed(int keyCode) {
                    return Gdx.input.isKeyPressed(keyCode);
                }
            });


    @Override
    public void create() {
        resources = new GameResources();
        renderer = new GameRenderer(resources);
        TiledMapTileLayer groundLayer = getSingleLayer(resources.getLevel());
        var playerGraphics = new TextureRegion(resources.getBlueTankTexture());
        var treeObstacleGraphics = new TextureRegion(resources.getGreenTreeTexture());
        Tank tank = new Tank(PLAYER_START, playerGraphics, PLAYER_SPEED);
        List<Obstacle> obstacles = new ArrayList<>();
        for (GridPoint2 position : TREE_POSITIONS) {
            Obstacle obstacle = new Obstacle(position);
            obstacles.add(obstacle);
            obstacleDrawables.add(new ObstacleDrawable(obstacle, treeObstacleGraphics, groundLayer));
        }
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        world = new GameWorld(tank, obstacles, tileMovement);

    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        playerInput.handleInput(world.getTank(), world.getGameField());

        world.update(deltaTime);
        renderer.render(world.getTank(), obstacleDrawables);
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
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        resources.dispose();
        renderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
