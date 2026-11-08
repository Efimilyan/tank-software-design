package ru.mipt.bit.platformer.input;

@FunctionalInterface
public interface JustPressedKeys {
    boolean isKeyJustPressed(int keyCode);
}