package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.RandomControlStrategy;
import ru.mipt.bit.platformer.control.TankController;
import ru.mipt.bit.platformer.level.ControlType;
import ru.mipt.bit.platformer.level.LevelData;
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

public class GameWorldFactory {
    private final float tankSpeed;
    private final ControlStrategy playerStrategy;
    private final Random random;

    public GameWorldFactory(float tankSpeed, ControlStrategy playerStrategy, Random random) {
        this.tankSpeed = tankSpeed;
        this.playerStrategy = playerStrategy;
        this.random = random;
    }

    public WorldAssembly create(LevelData level, int fieldWidth, int fieldHeight) {
        List<TankSpec> specs = level.tanks();
        List<Tank> tanks = new ArrayList<>();
        for (TankSpec spec : specs) {
            tanks.add(new Tank(spec.position(), tankSpeed));
        }
        List<Obstacle> obstacles = new ArrayList<>();
        for (GridPoint2 position : level.treePositions()) {
            obstacles.add(new Obstacle(position));
        }

        CollisionChecker collisionChecker =
                createCollisionChecker(fieldWidth, fieldHeight, tanks, obstacles);

        List<GameObject> gameObjects = new ArrayList<>();
        for (int i = 0; i < tanks.size(); i++) {
            ControlStrategy strategy = strategyFor(specs.get(i).control());
            gameObjects.add(new TankController(tanks.get(i), strategy, collisionChecker));
        }
        gameObjects.addAll(tanks);
        gameObjects.addAll(obstacles);

        return new WorldAssembly(new GameWorld(gameObjects), tanks, obstacles);
    }

    private CollisionChecker createCollisionChecker(
            int fieldWidth, int fieldHeight, List<Tank> tanks, List<Obstacle> obstacles) {
        List<Blocking> blockers = new ArrayList<>(obstacles);
        blockers.addAll(tanks);
        return new CompositeCollisionChecker(List.of(
                new FieldBounds(fieldWidth, fieldHeight),
                new GameField(blockers)));
    }

    private ControlStrategy strategyFor(ControlType type) {
        return switch (type) {
            case PLAYER -> playerStrategy;
            case RANDOM -> new RandomControlStrategy(random);
        };
    }
}
