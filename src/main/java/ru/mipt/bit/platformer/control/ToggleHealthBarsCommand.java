package ru.mipt.bit.platformer.control;

public class ToggleHealthBarsCommand implements GlobalCommand {
    private final Toggleable healthBars;

    public ToggleHealthBarsCommand(Toggleable healthBars) {
        this.healthBars = healthBars;
    }

    @Override
    public void execute() {
        healthBars.toggle();
    }
}