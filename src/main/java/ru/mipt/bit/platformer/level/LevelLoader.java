package ru.mipt.bit.platformer.level;

public interface LevelLoader {
    LevelData load(int fieldWidth, int fieldHeight);
}