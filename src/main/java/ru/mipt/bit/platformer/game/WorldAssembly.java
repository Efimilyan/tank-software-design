package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.model.GameWorld;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

public record WorldAssembly(GameWorld world, List<Tank> tanks, List<Obstacle> obstacles) {
}