package ru.mipt.bit.platformer.model.api;

import com.badlogic.gdx.math.GridPoint2;

public interface BattleField extends CollisionChecker, HitReceiver {
    static BattleField of(CollisionChecker checker, HitReceiver receiver) {
        return new BattleField() {
            @Override
            public boolean isOccupied(GridPoint2 coordinates) {
                return checker.isOccupied(coordinates);
            }

            @Override
            public void hit(GridPoint2 coordinates, int damage) {
                receiver.hit(coordinates, damage);
            }
        };
    }
}