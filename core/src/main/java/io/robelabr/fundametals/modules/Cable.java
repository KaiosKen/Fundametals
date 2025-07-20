package io.robelabr.fundametals.modules;

import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * A wire that transports information, must have the same input and output, to
 * create a different source and destination use adaptors
 * @param <T>
 */
public class Cable<T> {
    private Port<T> source, destination;
    List<Point2D> waypoints;

    public Cable(Port<T> input, Port<T> output) {
        this.source = input;
        this.destination = output;
    }

    /** Gets the source port connected to the cable, or null if there isn't one */
    public Port<T> getSource() { return source; }

    /** Sets the source port (Must be of the same type as the Cable) */
    public void setSource(Port<T> source) { this.source = source; }

    /** Gets the destination port connected to the cable, or null if there isn't one */
    public Port<T> getDestination() { return destination; }

    /** Sets the destination port (Must be of the same type as the Cable) */
    public void setDestination(Port<T> destination) { this.destination = destination; }

    /** TODO: Recomputes the poly-line whenever either endpoint moves. */
    public void recalcPath() { }

    /**
     * Grabs the value of the source port and sets the destination port, returning the value set
     * @return the {@link T} set to the destination.
     */
    public T calculateSignal() {
        T result = source.getSignal();
        destination.setSignal(result);
        return destination.getSignal();
    }

    /**
     * Reverses the source and destination port to allow for 2 way cable usage.
     * @return the {@link T} set to the source
     */
    public T calculateInverse() {
        T result = destination.getSignal();
        source.setSignal(result);
        return source.getSignal();
    }

    /**
     * Draws the cable from the source to the destination
     * @param tinyDrawer the {@link TinyVGShapeDrawer} used to draw the svg
     */
    public void draw(TinyVGShapeDrawer tinyDrawer) {

    }
}
