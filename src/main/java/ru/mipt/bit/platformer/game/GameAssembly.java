package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.Controllers;
import ru.mipt.bit.platformer.graphics.Drawable;
import ru.mipt.bit.platformer.graphics.GameRenderer;
import ru.mipt.bit.platformer.graphics.GameResources;
import ru.mipt.bit.platformer.graphics.HealthBar;
import ru.mipt.bit.platformer.graphics.HealthBarDrawable;
import ru.mipt.bit.platformer.graphics.HealthBarLayout;
import ru.mipt.bit.platformer.graphics.LevelView;
import ru.mipt.bit.platformer.graphics.MovableDrawable;
import ru.mipt.bit.platformer.graphics.ObstacleDrawable;
import ru.mipt.bit.platformer.graphics.ResourcePaths;
import ru.mipt.bit.platformer.graphics.TankHealthBar;
import ru.mipt.bit.platformer.graphics.TileMovement;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.LevelProvider;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.api.HasHealth;

import java.util.Random;

import static ru.mipt.bit.platformer.graphics.GdxGameUtils.getSingleLayer;

public class GameAssembly {
    private static final float HEALTH_BAR_HEIGHT = 8f;
    private static final float HEALTH_BAR_GAP = 4f;
    private static final Color HEALTH_BAR_BACKGROUND = Color.DARK_GRAY;
    private static final Color HEALTH_BAR_FILL = Color.GREEN;

    private final LevelProvider levelProvider;
    private final GameSettings settings;
    private final ResourcePaths paths;
    private final ControlStrategyRegistry strategies;
    private final Random random;

    public GameAssembly(LevelProvider levelProvider, GameSettings settings, ResourcePaths paths,
                        ControlStrategyRegistry strategies, Random random) {
        this.levelProvider = levelProvider;
        this.settings = settings;
        this.paths = paths;
        this.strategies = strategies;
        this.random = random;
    }

    public MainCycle assemble() {
        GameResources resources = new GameResources(paths);
        GameRenderer renderer = new GameRenderer(resources);
        TiledMapTileLayer groundLayer = getSingleLayer(resources.getLevel());
        int fieldWidth = groundLayer.getWidth();
        int fieldHeight = groundLayer.getHeight();
        LevelData levelData = levelProvider.load(fieldWidth, fieldHeight);

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
        HealthBar healthBar = createHealthBar(resources);

        LevelView levelView = new LevelView();
        levelView.register(Obstacle.class,
                obstacle -> new ObstacleDrawable(obstacle, treeObstacleGraphics, groundLayer));
        levelView.register(Tank.class,
                tank -> withHealthBar(tank,
                        new MovableDrawable(tank, tankGraphics, tileMovement), healthBar));
        levelView.register(Bullet.class,
                bullet -> new MovableDrawable(bullet, bulletGraphics, tileMovement));
        return levelView;
    }

    private HealthBar createHealthBar(GameResources resources) {
        TextureRegion pixel = new TextureRegion(resources.getPixelTexture());
        HealthBarLayout layout = new HealthBarLayout(HEALTH_BAR_HEIGHT, HEALTH_BAR_GAP);
        return new TankHealthBar(pixel, layout, HEALTH_BAR_BACKGROUND, HEALTH_BAR_FILL);
    }

    private static Drawable withHealthBar(HasHealth subject, MovableDrawable drawable,
                                          HealthBar healthBar) {
        return new HealthBarDrawable(drawable, healthBar, drawable::getRectangle, subject);
    }

    private Level createLevel(LevelView levelView, Controllers controllers, LevelData levelData,
                              int fieldWidth, int fieldHeight) {
        Level level = new Level();
        level.subscribe(levelView);
        new LevelPopulator(settings, strategies,
                new RandomHealthSupplier(random, settings.minTankHealth(), settings.maxTankHealth()))
                .populate(level, controllers, levelData, fieldWidth, fieldHeight);
        return level;
    }
}