package io.robelabr.fundametals.modules;

import com.badlogic.gdx.math.Vector2;
import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Class that connects components and their I/O's using wires
 * @author Robel Abraham
 */
public class Port<T> {
    private final int id;
    private Point2D.Float center;
    private Direction facing;
    private T signal;
    private boolean connected;
    private static int idCounter = 0;
    /** Run-time token that survives generic erasure. */
    private final Class<T> signalType;

    public Port(Point2D.Float center, Direction facing, Class<T> signalType) {
        this.id = ++idCounter;
        this.center = center;
        this.facing = facing;
        this.signalType = Objects.requireNonNull(signalType, "signalType");
    }

    public int getID() {
        return id;
    }

    public T getSignal() {
        return signal;
    }

    public void setSignal(T newSignal) {
        signal = newSignal;
    }

    // This is prolly dumb lmao
    public void connect() { connected = true; }
    public void disconnect() { connected = false; }
    public boolean isConnected() { return connected; }

    /** Checks a bounding rect to see if a point is inside of it. Used in InputHandler */
    public boolean contains(Point2D.Float worldPos) {
        TinyVG sprite = getSprite();
        float size = sprite.getScaledWidth() * sprite.getScaledHeight();
        Point2D.Float pos = new Point2D.Float(sprite.getPositionX(), sprite.getPositionY());
        return (worldPos.x >= pos.x && worldPos.x <= pos.x + sprite.getScaledWidth() && worldPos.y >= pos.y && worldPos.y <= pos.y + size);
    }

    /**
     * Returns {@code true} when {@code other} transports the *same*
     * signal type as this port — nothing more, nothing less.
     *
     * <p>Extra rules (e.g. IN → OUT only) can be layered on top:
     * <pre>{@code
     * return dir != other.dir              // opposite directions
     *        && signalType.equals(other.signalType);
     * }</pre>
     */
    public boolean compatibleWith(Port<?> other) {
        return other != null && signalType.equals(other.signalType);
    }

    /**
     * TODO: World-space coordinate of the port (updates automatically when
     * TODO: its parent component moves or rotates).
     */
    public Vector2 getWorldPosition() { return null; }

    /** TODO: Called by Cable when a connection is formed. */
    public void addConnection(Cable<T> cable) {  }

    /** TODO: Direction the port “faces” relative to its component (N, S, E, W). */
    public Direction getDirection() { return null; }

    /**
     * Gets the center point of the port, for drag calculations
     * @return a Point2D representation of a coordinate location
     */
    public Point2D getCenter() {
        return center;
    }

    /**
     * Gets the direction the port is facing, important for rendering.
     * @return A Direction
     */
    public Direction getFacing() {
        return facing;
    }

    /**
     * Gets the port tvg, small rounded square.
     * @return A sprite of the port
     */
    public TinyVG getSprite() {
        return Assets.getInstance().get("inputs/port.tvg", TinyVG.class);
    }

    /**
     * Updates the direction and position stored in the object. If Null is passed, nothing is changed
     * @param newPos the new {@link Point2D } position to set.
     * @param newDirection the new {@link Direction } to set.
     */
    public void move(Point2D.Float newPos, Direction newDirection) {
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
        return obj instanceof Port && id == ((Port<?>) obj).id;
    }



}
