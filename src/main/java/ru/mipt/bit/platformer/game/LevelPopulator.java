package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.Controllers;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.TankSpec;
import ru.mipt.bit.platformer.model.*;
import ru.mipt.bit.platformer.model.api.BattleField;
import ru.mipt.bit.platformer.model.api.CollisionChecker;
import ru.mipt.bit.platformer.model.api.GridPosition;
import ru.mipt.bit.platformer.model.api.Gun;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

public class LevelPopulator {
    private final GameSettings settings;
    private final ControlStrategyRegistry strategies;
    private final IntSupplier tankHealth;
    private final BulletSettings bulletSettings;

    public LevelPopulator(GameSettings settings, ControlStrategyRegistry strategies, IntSupplier tankHealth) {
        this.settings = settings;
        this.strategies = strategies;
        this.tankHealth = tankHealth;
        this.bulletSettings = BulletSettings.of(settings.bulletType(), settings.nominalTankHealth());
    }

    public void populate(Level level, Controllers controllers, LevelData data,
                         int fieldWidth, int fieldHeight) {
        GameField field = new GameField();
        level.subscribe(field);
        CollisionChecker collisionChecker = new CompositeCollisionChecker(List.of(
                new FieldBounds(fieldWidth, fieldHeight), field));
        BattleField battleField = BattleField.of(collisionChecker, field);

        List<Tank> tanks = createTanks(level, battleField, data.tanks());
        addObstacles(level, data.treePositions());
        addControllers(controllers, tanks, data.tanks(), collisionChecker);
        tanks.forEach(level::add);
    }

    private List<Tank> createTanks(Level level, BattleField battleField, List<TankSpec> specs) {
        List<Tank> tanks = new ArrayList<>();
        for (TankSpec spec : specs) {
            Gun gun = new BulletGun(level::add, battleField, bulletSettings.speed(),
                    bulletSettings.damage(), settings.reloadTime());
            tanks.add(new Tank(spec.position(), settings.tankSpeed(), tankHealth.getAsInt(), gun));
        }
        return tanks;
    }

    private void addObstacles(Level level, List<GridPosition> positions) {
        for (GridPosition position : positions) {
            level.add(new Obstacle(position));
        }
    }

    private void addControllers(Controllers controllers, List<Tank> tanks, List<TankSpec> specs,
                                CollisionChecker collisionChecker) {
        for (int i = 0; i < tanks.size(); i++) {
            ControlStrategy strategy = strategies.create(specs.get(i).control());
            controllers.add(new TankController(tanks.get(i), strategy, collisionChecker));
        }
    }
}