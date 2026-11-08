package ru.mipt.bit.platformer.control;

public class GlobalCommandController implements Controller {
    private final GlobalCommandSource source;

    public GlobalCommandController(GlobalCommandSource source) {
        this.source = source;
    }

    @Override
    public void update(float deltaTime) {
        for (GlobalCommand command : source.pendingCommands()) {
            command.execute();
        }
    }

    @Override
    public boolean isAlive() {
        return true;
    }
}