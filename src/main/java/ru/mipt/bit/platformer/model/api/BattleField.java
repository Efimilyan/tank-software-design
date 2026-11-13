package ru.mipt.bit.platformer.model.api;

public interface BattleField extends CollisionChecker, HitReceiver {
    static BattleField of(CollisionChecker checker, HitReceiver receiver) {
        return new BattleField() {
            @Override
            public boolean isOccupied(GridPosition coordinates) {
                return checker.isOccupied(coordinates);
            }

            @Override
            public void hit(GridPosition coordinates, int damage) {
                receiver.hit(coordinates, damage);
            }
        };
    }
}