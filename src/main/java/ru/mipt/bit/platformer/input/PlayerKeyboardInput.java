package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.CollisionChecker;

import ru.mipt.bit.platformer.model.Tank;

import static com.badlogic.gdx.Input.Keys.*;

public class PlayerKeyboardInput {
    private final KeyStateProvider keys;

    public PlayerKeyboardInput(KeyStateProvider keys) {
        this.keys = keys;
    }

    public void handleInput(Tank tank, CollisionChecker collisionChecker) {
        if (keys.isKeyPressed(UP) || keys.isKeyPressed(W)) {
            tank.move(Tank.Direction.UP, collisionChecker);
        }
        if (keys.isKeyPressed(DOWN) || keys.isKeyPressed(S)) {
            tank.move(Tank.Direction.DOWN, collisionChecker);
        }
        if (keys.isKeyPressed(LEFT) || keys.isKeyPressed(A)) {
            tank.move(Tank.Direction.LEFT, collisionChecker);
        }
        if (keys.isKeyPressed(RIGHT) || keys.isKeyPressed(D)) {
            tank.move(Tank.Direction.RIGHT, collisionChecker);
        }
    }
    }