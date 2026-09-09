package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

import static com.badlogic.gdx.Input.Keys.*;


public class PlayerKeyboardInput {

    public void handleInput(Tank tank, GameField gameField) {
        if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) tank.moveUp(gameField);
        if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) tank.moveDown(gameField);
        if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) tank.moveLeft(gameField);
        if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) tank.moveRight(gameField);
    }
}
