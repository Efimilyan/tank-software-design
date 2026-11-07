package ru.mipt.bit.platformer.control;

public interface ControlStrategy {
    TankCommand nextCommand(float deltaTime);
}