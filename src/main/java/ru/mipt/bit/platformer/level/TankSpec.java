package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.api.GridPosition;

public record TankSpec(GridPosition position, ControlType control) {
}