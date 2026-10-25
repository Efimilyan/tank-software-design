package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class CompositeCollisionChecker implements CollisionChecker {
    private final List<CollisionChecker> checkers;

    public CompositeCollisionChecker(List<CollisionChecker> checkers) {
        this.checkers = List.copyOf(checkers);
    }

    @Override
    public boolean isOccupied(GridPoint2 coordinates) {
        for (CollisionChecker checker : checkers) {
            if (checker.isOccupied(coordinates)) {
                return true;
            }
        }
        return false;
    }
}