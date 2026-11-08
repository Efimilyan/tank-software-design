package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.control.TankCommand;

public record TankKeyBinding(TankCommand command, int... keyCodes) {
    boolean isPressed(KeyStateProvider keys) {
        for (int keyCode : keyCodes) {
            if (keys.isKeyPressed(keyCode)) {
                return true;
            }
        }
        return false;
    }
}