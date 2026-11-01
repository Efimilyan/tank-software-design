package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class GameField implements CollisionChecker, LevelListener {
    private final List<Blocking> obstacles;

    public GameField() {
        this(List.of());
    }

    public GameField(List<? extends Blocking> obstacles) {
        this.obstacles = new ArrayList<>(obstacles);
    }

    @Override
    public boolean isOccupied(GridPoint2 coordinates) {
        for (int i = 0; i < obstacles.size(); i++) {
            Blocking obstacle = obstacles.get(i);
            if (obstacle.occupies(coordinates)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void objectAdded(GameObject object) {
        if (object instanceof Blocking blocking) {
            obstacles.add(blocking);
        }
    }

    @Override
    public void objectRemoved(GameObject object) {
        if (object instanceof Blocking blocking) {
            obstacles.remove(blocking);
        }
    }
}
