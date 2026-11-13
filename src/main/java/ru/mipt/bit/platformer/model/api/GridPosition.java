package ru.mipt.bit.platformer.model.api;

public record GridPosition(int x, int y) {
    public GridPosition shifted(int deltaX, int deltaY) {
        return new GridPosition(x + deltaX, y + deltaY);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}