package ru.mipt.bit.platformer.model.api;

public interface Blocking {
    boolean occupies(GridPosition position);

    void hit(int damage);
}
