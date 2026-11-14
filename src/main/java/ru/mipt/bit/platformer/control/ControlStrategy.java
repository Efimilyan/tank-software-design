package ru.mipt.bit.platformer.control;

/** Порт: источник команд для танка. */
public interface ControlStrategy {
    TankCommand nextCommand(float deltaTime);
}