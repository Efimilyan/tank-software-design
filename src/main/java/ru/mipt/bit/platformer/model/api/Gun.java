package ru.mipt.bit.platformer.model.api;

public interface Gun {
    Gun NONE = new Gun() {
        @Override
        public void update(float deltaTime) {
        }

        @Override
        public void fire(GridPosition from, Direction direction) {
        }
    };

    void update(float deltaTime);

    void fire(GridPosition from, Direction direction);
}
