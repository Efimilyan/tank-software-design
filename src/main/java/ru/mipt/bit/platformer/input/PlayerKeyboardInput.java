package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

import static com.badlogic.gdx.Input.Keys.*;

public class PlayerKeyboardInput {
    private final KeyStateProvider keys;

    public PlayerKeyboardInput(KeyStateProvider keys) {
        this.keys = keys;
    }

    public void handleInput(Tank tank, GameField gameField) {
        if (keys.isKeyPressed(UP) || keys.isKeyPressed(W)) {
            tank.move(Tank.Direction.UP, gameField);
        }
        if (keys.isKeyPressed(DOWN) || keys.isKeyPressed(S)) {
            tank.move(Tank.Direction.DOWN, gameField);
        }
        if (keys.isKeyPressed(LEFT) || keys.isKeyPressed(A)) {
            tank.move(Tank.Direction.LEFT, gameField);
        }
        if (keys.isKeyPressed(RIGHT) || keys.isKeyPressed(D)) {
            tank.move(Tank.Direction.RIGHT, gameField);
        }
    }
}