package ru.mipt.bit.platformer.graphics;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GameObject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelViewTest {
    private static class Wall implements GameObject {
        @Override
        public void update(float deltaTime) {
        }
    }

    private static class Ghost implements GameObject {
        @Override
        public void update(float deltaTime) {
        }
    }

    private final LevelView view = new LevelView();

    @Test
    void createsDrawableForRegisteredObjectType() {
        Wall wall = new Wall();
        Drawable drawable = batch -> { };
        view.register(Wall.class, created -> created == wall ? drawable : null);

        view.objectAdded(wall);

        assertEquals(List.of(drawable), List.copyOf(view.drawables()));
    }

    @Test
    void ignoresObjectTypeWithoutRegisteredGraphics() {
        view.register(Wall.class, wall -> batch -> { });

        view.objectAdded(new Ghost());

        assertTrue(view.drawables().isEmpty());
    }

    @Test
    void dropsDrawableWhenObjectIsRemoved() {
        Wall wall = new Wall();
        view.register(Wall.class, created -> batch -> { });
        view.objectAdded(wall);

        view.objectRemoved(wall);

        assertTrue(view.drawables().isEmpty());
    }

    @Test
    void keepsDrawablesInOrderOfAddition() {
        Drawable first = batch -> { };
        Drawable second = batch -> { };
        Wall firstWall = new Wall();
        Wall secondWall = new Wall();
        view.register(Wall.class, wall -> wall == firstWall ? first : second);

        view.objectAdded(firstWall);
        view.objectAdded(secondWall);

        assertEquals(List.of(first, second), List.copyOf(view.drawables()));
    }
}
