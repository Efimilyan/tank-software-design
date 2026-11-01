package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelListener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class LevelView implements LevelListener {
    private final Map<Class<? extends GameObject>, Function<GameObject, Drawable>> creators =
            new HashMap<>();
    private final Map<GameObject, Drawable> drawables = new LinkedHashMap<>();

    public <T extends GameObject> void register(
            Class<T> type, Function<? super T, ? extends Drawable> creator) {
        creators.put(type, object -> creator.apply(type.cast(object)));
    }

    @Override
    public void objectAdded(GameObject object) {
        Function<GameObject, Drawable> creator = creators.get(object.getClass());
        if (creator != null) {
            drawables.put(object, creator.apply(object));
        }
    }

    @Override
    public void objectRemoved(GameObject object) {
        drawables.remove(object);
    }

    public Collection<Drawable> drawables() {
        return Collections.unmodifiableCollection(drawables.values());
    }
}
