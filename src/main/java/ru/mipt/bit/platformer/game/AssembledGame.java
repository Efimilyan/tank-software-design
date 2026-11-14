package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.control.MainCycle;

public record AssembledGame(MainCycle mainCycle, Disposable view) {
}