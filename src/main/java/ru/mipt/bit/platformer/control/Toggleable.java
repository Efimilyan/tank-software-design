package ru.mipt.bit.platformer.control;

/** Порт: переключение режима отображения. */
@FunctionalInterface
public interface Toggleable {
    void toggle();
}