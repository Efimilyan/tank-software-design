package ru.mipt.bit.platformer.input;

/** Порт: состояние клавиш. */
public interface KeyStateProvider {
    boolean isKeyPressed(int keyCode);
}