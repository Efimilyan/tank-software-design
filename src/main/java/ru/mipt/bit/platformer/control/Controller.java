package ru.mipt.bit.platformer.control;

public interface Controller {
    void update(float deltaTime);

    boolean isAlive();
}