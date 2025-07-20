package io.robelabr.fundametals.modules;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import io.robelabr.fundametals.Renderer;
import io.robelabr.fundametals.graphics.Assets;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * A component is a piece of tech that takes in a signal and produces an output
 * @author Robel Abraham
 */
public abstract class Component<T> {
//    private final Map<Integer,Port<T>> ports;
    protected final List<Port<T>> ports;
    protected T output;
    protected Point2D.Float pos = new Point2D.Float();

    public Component() {
        ports = new ArrayList<>();
    }

    /** Sets the world position of the component */
    public void setPosition(float x, float y) {
        this.pos.x = x;
        this.pos.y = y;
    }
    public void setPosition(Point2D.Float pos) {
        this.pos = (Point2D.Float) pos.clone();
    }

    public Point2D.Float getPosition() { return pos; }

    /** Checks a bounding rect to see if a point is inside of it. Used in InputHandler */
    public boolean contains(Point2D.Float worldPos) {
        TinyVG sprite = getSprite();
        float size = sprite.getScaledWidth() * sprite.getScaledHeight();
        Point2D.Float pos = new Point2D.Float(sprite.getPositionX(), sprite.getPositionY());
        return (worldPos.x >= pos.x && worldPos.x <= pos.x + sprite.getScaledWidth() && worldPos.y >= pos.y && worldPos.y <= pos.y + size);
    }

    /** Returns an immutable view of all ports for this component. */
    public List<Port<T>> getPorts() { return ports; }

    /** TODO: Groups selected components under a new visual “box” and name. */
    public static <T> Component<T> box(String label, Collection<Component<T>> parts) { return null; }

    /** TODO: Check all ports and their hitbox to see if worldPos intersects with it. */
    public Port<?> findPortAt(Point2D.Float worldPos) {
        for (Port<T> port : ports) {
            if (port.contains(worldPos)) {
                return port;
            }
        }
        return null;
    }

    public abstract Component<T> copy();

    public void addPort(Port<T> port) {
        this.ports.add(port);
    }

    public Port<T> removePort(int index) {
        return this.ports.remove(index);
    }

    public Port<T> getPort(int index) {
        return ports.get(index);
    }


    /** TODO: Updates the component live every tick, scaling by delta time to account for fps */
    public void update(float delta) {    }
    /**
     * Small icon used in the right-hand palette and when zoomed-out.
     * Override in concrete subclasses that have different art.
     * If you don't override, a default icon will be shown
     */
    public static TinyVG getIcon() {
        return Assets.getInstance().get("defaults/question_mark.tvg", TinyVG.class);
    }

    /**
     * Gets the current in-world Tvg of the component, affected by its state.
     * @return The sprite for the component
     */
    public abstract TinyVG getSprite();

    /**
     * Renders the component.
     *
     * @param drawer  the TinyVgShapeDrawer already begun by {@link Renderer}
     * @param zoom   TODO: current camera zoom; use this to decide whether to show
     *               icon-only (zoomed out) or full transistor detail.
     */
    public abstract void draw(TinyVGShapeDrawer drawer, float zoom);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
