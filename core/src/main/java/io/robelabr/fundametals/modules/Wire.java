package io.robelabr.fundametals.modules;

import java.awt.geom.Point2D;
import java.util.List;

public class Wire {

    private Port input, output;
    List<Point2D> waypoints;

    Wire(Port input, Port output) {
        this.input = input;
        this.output = output;
    }


}
