package ru.mipt.bit.platformer.level;

/** Порт: загрузка уровня. */
public interface LevelProvider {
    LevelData load(int fieldWidth, int fieldHeight);
}