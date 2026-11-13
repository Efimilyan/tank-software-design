package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.model.api.CollisionChecker;
import ru.mipt.bit.platformer.model.api.GridPosition;

import java.util.List;

public class CompositeCollisionChecker implements CollisionChecker {
    private final List<CollisionChecker> checkers;

    public CompositeCollisionChecker(List<CollisionChecker> checkers) {
        this.checkers = List.copyOf(checkers);
    }

    @Override
    public boolean isOccupied(GridPosition coordinates) {
        for (CollisionChecker checker : checkers) {
            if (checker.isOccupied(coordinates)) {
                return true;
            }
        }
        return false;
    }
}