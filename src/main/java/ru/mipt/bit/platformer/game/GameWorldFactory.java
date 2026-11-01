package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.RandomControlStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.level.ControlType;
import ru.mipt.bit.platformer.level.LevelData;
import ru.mipt.bit.platformer.level.TankSpec;
import ru.mipt.bit.platformer.model.BulletGun;
import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.CompositeCollisionChecker;
import ru.mipt.bit.platformer.model.FieldBounds;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Gun;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWorldFactory {
    private final GameSettings settings;
    private final ControlStrategy playerStrategy;
    private final Random random;

    public GameWorldFactory(GameSettings settings, ControlStrategy playerStrategy, Random random) {
        this.settings = settings;
        this.playerStrategy = playerStrategy;
        this.random = random;
    }

    public void populate(Level level, LevelData data, int fieldWidth, int fieldHeight) {
        GameField field = new GameField();
        level.subscribe(field);
        CollisionChecker collisionChecker = new CompositeCollisionChecker(List.of(
                new FieldBounds(fieldWidth, fieldHeight), field));

        List<TankSpec> specs = data.tanks();
        List<Tank> tanks = new ArrayList<>();
        for (TankSpec spec : specs) {
            Gun gun = new BulletGun(level, collisionChecker, settings.bulletSpeed(),
                    settings.bulletDamage(), settings.reloadTime());
            tanks.add(new Tank(spec.position(), settings.tankSpeed(), settings.tankHealth(), gun));
        }

        for (GridPoint2 position : data.treePositions()) {
            level.add(new Obstacle(position));
        }
        for (int i = 0; i < tanks.size(); i++) {
            ControlStrategy strategy = strategyFor(specs.get(i).control());
            level.add(new TankController(tanks.get(i), strategy, collisionChecker));
        }
        for (Tank tank : tanks) {
            level.add(tank);
        }
    }

    private ControlStrategy strategyFor(ControlType type) {
        return switch (type) {
            case PLAYER -> playerStrategy;
            case RANDOM -> new RandomControlStrategy(random, settings.enemyShootProbability());
        };
    }
}
