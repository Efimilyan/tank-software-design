package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.control.Toggleable;
import ru.mipt.bit.platformer.graphics.Visibility;

/** Адаптер Toggleable: видимость полосок здоровья. */
public class HealthBarSwitch implements Visibility, Toggleable {
    private boolean visible;

    public HealthBarSwitch(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void toggle() {
        visible = !visible;
    }
}