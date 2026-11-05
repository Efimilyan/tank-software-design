package ru.mipt.bit.platformer.level;

public interface LevelProvider {
    LevelData load(int fieldWidth, int fieldHeight);
}