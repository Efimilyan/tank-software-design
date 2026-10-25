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
import ru.mipt.bit.platformer.control.RandomControlStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.graphics.*;
import ru.mipt.bit.platformer.input.KeyboardControlStrategy;
import ru.mipt.bit.platformer.level.ControlType;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.LevelLoaderSwitch;
import ru.mipt.bit.platformer.level.TankSpec;
import ru.mipt.bit.platformer.model.Blocking;
import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.CompositeCollisionChecker;
import ru.mipt.bit.platformer.model.FieldBounds;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.GameObject;
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
    private final Random random = new Random();
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

        LevelData level = levelLoader.load(groundLayer.getWidth(), groundLayer.getHeight());

        List<TankSpec> tankSpecs = level.tanks();
        List<Tank> tanks = new ArrayList<>();
        for (TankSpec spec : tankSpecs) {
            tanks.add(new Tank(spec.position(), TANK_SPEED));
        }

        List<Obstacle> obstacles = new ArrayList<>();
        for (GridPoint2 position : level.treePositions()) {
            Obstacle obstacle = new Obstacle(position);
            obstacles.add(obstacle);
            drawables.add(new ObstacleDrawable(obstacle, treeObstacleGraphics, groundLayer));
        }

        List<Blocking> blockers = new ArrayList<>(obstacles);
        blockers.addAll(tanks);
        CollisionChecker collisionChecker = new CompositeCollisionChecker(List.of(
                new FieldBounds(groundLayer.getWidth(), groundLayer.getHeight()),
                new GameField(blockers)));

        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        List<GameObject> gameObjects = new ArrayList<>();
        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i);
            ControlStrategy strategy = strategyFor(tankSpecs.get(i).control());
            gameObjects.add(new TankController(tank, strategy, collisionChecker));
            drawables.add(new TankDrawable(tank, tankGraphics, tileMovement));
        }
        gameObjects.addAll(tanks);
        gameObjects.addAll(obstacles);
        world = new GameWorld(gameObjects);
    }

    private ControlStrategy strategyFor(ControlType type) {
        return switch (type) {
            case PLAYER -> playerStrategy;
            case RANDOM -> new RandomControlStrategy(random);
        };
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