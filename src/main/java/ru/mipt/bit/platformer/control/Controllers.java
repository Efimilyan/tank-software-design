package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.List;

public class Controllers {
    private final List<Controller> controllers = new ArrayList<>();

    public void add(Controller controller) {
        controllers.add(controller);
    }

    public int size() {
        return controllers.size();
    }

    public void update(float deltaTime) {
        for (Controller controller : List.copyOf(controllers)) {
            if (controller.isAlive()) {
                controller.update(deltaTime);
            }
        }
        controllers.removeIf(controller -> !controller.isAlive());
    }
}