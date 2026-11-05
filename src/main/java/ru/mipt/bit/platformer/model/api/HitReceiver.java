package ru.mipt.bit.platformer.model.api;

import com.badlogic.gdx.math.GridPoint2;

@FunctionalInterface
public interface HitReceiver {
    void hit(GridPoint2 coordinates, int damage);
}