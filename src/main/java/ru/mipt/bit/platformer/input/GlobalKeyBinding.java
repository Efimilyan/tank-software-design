package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.GlobalCommand;

public record GlobalKeyBinding(GlobalCommand command, int... keyCodes) {
    boolean isJustPressed(JustPressedKeys keys) {
        for (int keyCode : keyCodes) {
            if (keys.isKeyJustPressed(keyCode)) {
                return true;
            }
        }
        return false;
    }
}