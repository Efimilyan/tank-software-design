package ru.mipt.bit.platformer.model.api;

public interface Blocking {
    boolean occupies(GridPosition position);

    default void hit(int damage) {
    }
}
