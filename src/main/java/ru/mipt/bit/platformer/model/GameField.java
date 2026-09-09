package ru.mipt.bit.platformer.model;


import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public class GameField {
    private List<Obstacle> obstacles;

    public GameField(List<Obstacle> obstacles){
        this.obstacles = obstacles;
    }

    public boolean isOccupied(GridPoint2 coordinates){
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
                if (obstacle.occupies(coordinates)) {
                    return true;
                }
        }
        return false;
    }
}
