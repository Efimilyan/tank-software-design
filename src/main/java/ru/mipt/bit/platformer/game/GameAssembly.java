package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.Controllers;
import ru.mipt.bit.platformer.control.GlobalCommandController;
import ru.mipt.bit.platformer.control.MainCycle;
import ru.mipt.bit.platformer.control.ToggleHealthBarsCommand;
import ru.mipt.bit.platformer.graphics.*;
import ru.mipt.bit.platformer.input.GlobalKeyBinding;
import ru.mipt.bit.platformer.input.HotkeyCommandSource;
import ru.mipt.bit.platformer.input.JustPressedKeys;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.LevelProvider;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.api.HasHealth;

import java.util.List;
import java.util.Random;

import static ru.mipt.bit.platformer.graphics.GdxGameUtils.getSingleLayer;

public class GameAssembly {
    private static final float HEALTH_BAR_HEIGHT = 8f;
    private static final float HEALTH_BAR_GAP = 4f;
    private static final Color HEALTH_BAR_BACKGROUND = Color.DARK_GRAY;
    private static final Color HEALTH_BAR_FILL = Color.GREEN;
    private static final boolean HEALTH_BARS_VISIBLE_AT_START = true;
    private static final int TOGGLE_HEALTH_BARS_KEY = Keys.L;

    private final LevelProvider levelProvider;
    private final GameSettings settings;
    private final ResourcePaths paths;
    private final ControlStrategyRegistry strategies;
    private final Random random;
    private final JustPressedKeys keys;

    public GameAssembly(LevelProvider levelProvider, GameSettings settings, ResourcePaths paths,
                        ControlStrategyRegistry strategies, Random random, JustPressedKeys keys) {
        this.levelProvider = levelProvider;
        this.settings = settings;
        this.paths = paths;
        this.strategies = strategies;
        this.random = random;
        this.keys = keys;
    }

    public AssembledGame assemble() {
        GameResources resources = new GameResources(paths);
        GameRenderer renderer = new GameRenderer(resources);
        TiledMapTileLayer groundLayer = getSingleLayer(resources.getLevel());
        int fieldWidth = groundLayer.getWidth();
        int fieldHeight = groundLayer.getHeight();
        LevelData levelData = levelProvider.load(fieldWidth, fieldHeight);

        HealthBarSwitch healthBarSwitch = new HealthBarSwitch(HEALTH_BARS_VISIBLE_AT_START);
        Controllers controllers = new Controllers();
        addGlobalCommands(controllers, healthBarSwitch);
        LevelView levelView = createLevelView(resources, groundLayer, healthBarSwitch);
        Level level = createLevel(levelView, controllers, levelData, fieldWidth, fieldHeight);
        GdxGameView view = new GdxGameView(resources, renderer, levelView);
        return new AssembledGame(new MainCycle(controllers, level, view), view);
    }

    private void addGlobalCommands(Controllers controllers, HealthBarSwitch healthBarSwitch) {
        GlobalKeyBinding toggleHealthBars = new GlobalKeyBinding(
                new ToggleHealthBarsCommand(healthBarSwitch), TOGGLE_HEALTH_BARS_KEY);
        controllers.add(new GlobalCommandController(
                new HotkeyCommandSource(keys, List.of(toggleHealthBars))));
    }

    private LevelView createLevelView(GameResources resources, TiledMapTileLayer groundLayer,
                                      Visibility healthBarVisibility) {
        var tankGraphics = new TextureRegion(resources.getTankTexture());
        var treeObstacleGraphics = new TextureRegion(resources.getGreenTreeTexture());
        var bulletGraphics = new TextureRegion(resources.getBulletTexture());
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        HealthBar healthBar = createHealthBar(resources, healthBarVisibility);

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

    private HealthBar createHealthBar(GameResources resources, Visibility visibility) {
        TextureRegion pixel = new TextureRegion(resources.getPixelTexture());
        HealthBarLayout layout = new HealthBarLayout(HEALTH_BAR_HEIGHT, HEALTH_BAR_GAP);
        HealthBar tankHealthBar = new TankHealthBar(
                pixel, layout, HEALTH_BAR_BACKGROUND, HEALTH_BAR_FILL);
        return new SwitchableHealthBar(tankHealthBar, visibility);
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