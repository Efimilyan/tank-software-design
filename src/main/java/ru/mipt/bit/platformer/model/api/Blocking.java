package ru.mipt.bit.platformer.model.api;

import com.badlogic.gdx.math.GridPoint2;

public interface Blocking {
    boolean occupies(GridPoint2 position);

    default void hit(int damage) {
    }
}
