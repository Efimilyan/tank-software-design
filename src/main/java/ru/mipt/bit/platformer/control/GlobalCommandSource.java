package ru.mipt.bit.platformer.control;

import java.util.List;

@FunctionalInterface
public interface GlobalCommandSource {
    List<GlobalCommand> pendingCommands();
}