package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Gun {
    Gun NONE = new Gun() {
        @Override
        public void update(float deltaTime) {
        }

        @Override
        public void fire(GridPoint2 from, Direction direction) {
        }
    };

    void update(float deltaTime);

    void fire(GridPoint2 from, Direction direction);
}
