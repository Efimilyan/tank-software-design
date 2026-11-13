package ru.mipt.bit.platformer.model.api;

@FunctionalInterface
public interface HitReceiver {
    void hit(GridPosition coordinates, int damage);
}