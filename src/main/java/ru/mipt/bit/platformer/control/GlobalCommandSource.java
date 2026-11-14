package ru.mipt.bit.platformer.control;

import java.util.List;

/** Порт: источник глобальных команд. */
@FunctionalInterface
public interface GlobalCommandSource {
    List<GlobalCommand> pendingCommands();
}