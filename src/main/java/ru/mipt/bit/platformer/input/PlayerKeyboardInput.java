package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

import static com.badlogic.gdx.Input.Keys.*;


public class PlayerKeyboardInput {

    public void handleInput(Tank tank, GameField gameField) {
        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
            tank.move(Direction.UP, gameField);
        }
        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
            tank.move(Direction.DOWN, gameField);
        }
        if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
            tank.move(Direction.LEFT, gameField);
        }
        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
            tank.move(Direction.RIGHT, gameField);
        }
    }
}
