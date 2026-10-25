package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

public record TankSpec(GridPoint2 position, ControlType control) {
}