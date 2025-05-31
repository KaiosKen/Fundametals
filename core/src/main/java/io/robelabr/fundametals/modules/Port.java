package io.robelabr.fundametals.modules;

import java.awt.geom.Point2D;

public class Port {
    private int id;
    private Point2D center;
    private Direction facing;

    public Port(int id, Point2D center, Direction facing) {
        this.id = id;
        this.center = center;
        this.facing = facing;
    }

    public int getID() {
        return id;
    }

    public Point2D getCenter() {
        return center;
    }
    public Direction getFacing() {
        return facing;
    }

    public void move(Point2D newPos, Direction newDirection) {
        if (newDirection != null) {
            facing = newDirection;
        }
        if (newPos != null) {
            center = newPos;
        }
    }

    @Override
    public int hashCode() {
        return 31 * id + center.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Port && id == ((Port) obj).id;
    }


}
