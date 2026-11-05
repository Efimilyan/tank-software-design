package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.Controllers;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.GameResources;
import ru.mipt.bit.platformer.graphics.LevelView;
import ru.mipt.bit.platformer.graphics.MovableDrawable;
import ru.mipt.bit.platformer.graphics.ObstacleDrawable;
import ru.mipt.bit.platformer.graphics.ResourcePaths;
import ru.mipt.bit.platformer.graphics.TileMovement;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameAssembly {
    private final LevelLoader levelLoader;
    private final GameSettings settings;
    private final ResourcePaths paths;
    private final ControlStrategyRegistry strategies;

    public GameAssembly(LevelLoader levelLoader, GameSettings settings, ResourcePaths paths,
                        ControlStrategyRegistry strategies) {
        this.levelLoader = levelLoader;
        this.settings = settings;
        this.paths = paths;
        this.strategies = strategies;
    }

    public MainCycle assemble() {
        GameResources resources = new GameResources(paths);
        GameRenderer renderer = new GameRenderer(resources);
        TiledMapTileLayer groundLayer = getSingleLayer(resources.getLevel());
        int fieldWidth = groundLayer.getWidth();
        int fieldHeight = groundLayer.getHeight();
        LevelData levelData = levelLoader.load(fieldWidth, fieldHeight);

        Controllers controllers = new Controllers();
        LevelView levelView = createLevelView(resources, groundLayer);
        Level level = createLevel(levelView, controllers, levelData, fieldWidth, fieldHeight);
        return new MainCycle(resources, renderer, levelView, controllers, level);
    }

    private LevelView createLevelView(GameResources resources, TiledMapTileLayer groundLayer) {
        var tankGraphics = new TextureRegion(resources.getTankTexture());
        var treeObstacleGraphics = new TextureRegion(resources.getGreenTreeTexture());
        var bulletGraphics = new TextureRegion(resources.getBulletTexture());
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        LevelView levelView = new LevelView();
        levelView.register(Obstacle.class,
                obstacle -> new ObstacleDrawable(obstacle, treeObstacleGraphics, groundLayer));
        levelView.register(Tank.class,
                tank -> new MovableDrawable(tank, tankGraphics, tileMovement));
        levelView.register(Bullet.class,
                bullet -> new MovableDrawable(bullet, bulletGraphics, tileMovement));
        return levelView;
    }

    private Level createLevel(LevelView levelView, Controllers controllers, LevelData levelData,
                              int fieldWidth, int fieldHeight) {
        Level level = new Level();
        level.subscribe(levelView);
        new GameWorldFactory(settings, strategies)
                .populate(level, controllers, levelData, fieldWidth, fieldHeight);
        return level;
    }
}