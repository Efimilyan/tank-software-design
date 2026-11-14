package ru.mipt.bit.platformer.input;

/** Порт: только что нажатые клавиши. */
@FunctionalInterface
public interface JustPressedKeys {
    boolean isKeyJustPressed(int keyCode);
}