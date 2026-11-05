package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.level.ControlType;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class ControlStrategyRegistry {
    private final Map<ControlType, Supplier<ControlStrategy>> factories =
            new EnumMap<>(ControlType.class);

    public ControlStrategyRegistry register(ControlType type, Supplier<ControlStrategy> factory) {
        factories.put(type, factory);
        return this;
    }

    public ControlStrategy create(ControlType type) {
        Supplier<ControlStrategy> factory = factories.get(type);
        if (factory == null) {
            throw new IllegalStateException("Не зарегистрирована стратегия для " + type);
        }
        return factory.get();
    }
}