package io.robelabr.fundametals.modules;

public enum Direction {
    NORTH(0, -1),
    EAST (1,  0),
    SOUTH(0,  1),
    WEST (-1, 0);

    public final int dx;   // unit vector—handy for auto-routing
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /** 90° clockwise turn */
    public Direction turnRight() {
        return values()[(ordinal() + 1) & 3];
    }

    /** 90° counter-clockwise turn */
    public Direction turnLeft() {
        return values()[(ordinal() + 3) & 3];
    }

    /**
     * Creates a flipped version of the current object
     * @return an Object facing the opposing direction
     */
    public Direction opposite() {
        return values()[(ordinal() + 2) & 3];
    }
}

