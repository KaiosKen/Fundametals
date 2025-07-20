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
     * Returns the direction that is 180° opposite of this one.
     */
    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST  -> WEST;
            case WEST  -> EAST;
        };
    }

    /**
     * Rotates the direction 90 degrees clockwise.
     */
    public Direction rotate90() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST  -> SOUTH;
            case SOUTH -> WEST;
            case WEST  -> NORTH;
        };
    }

}

