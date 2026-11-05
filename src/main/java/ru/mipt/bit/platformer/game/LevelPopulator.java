package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.Controllers;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.TankSpec;
import ru.mipt.bit.platformer.model.api.BattleField;
import ru.mipt.bit.platformer.model.BulletGun;
import ru.mipt.bit.platformer.model.api.CollisionChecker;
import ru.mipt.bit.platformer.model.CompositeCollisionChecker;
import ru.mipt.bit.platformer.model.FieldBounds;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.api.Gun;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

public class LevelPopulator {
    private final GameSettings settings;
    private final ControlStrategyRegistry strategies;

    public LevelPopulator(GameSettings settings, ControlStrategyRegistry strategies) {
        this.settings = settings;
        this.strategies = strategies;
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
            Gun gun = new BulletGun(level::add, battleField, settings.bulletSpeed(),
                    settings.bulletDamage(), settings.reloadTime());
            tanks.add(new Tank(spec.position(), settings.tankSpeed(), settings.tankHealth(), gun));
        }
        return tanks;
    }

    private void addObstacles(Level level, List<GridPoint2> positions) {
        for (GridPoint2 position : positions) {
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