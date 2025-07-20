package io.robelabr.fundametals;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import io.robelabr.fundametals.modules.Cable;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.Port;

import java.awt.geom.Point2D;


/**
 * <p><strong>InputHandler</strong> centralises all user interactions: camera
 * navigation, component drag‑&‑drop, wire drawing, and box‑selection.  It uses
 * LibGDX’s {@link InputAdapter} base‑class so it can be registered directly
 * via <code>Gdx.input.setInputProcessor(...)</code>.</p>
 *
 * @author Robel Abraham
 */
public class InputHandler extends InputAdapter {

    // ---------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------

    private final World             world;
    private final OrthographicCamera camera;
    private final Renderer           renderer;

    // Camera drag panning
    private boolean    rightDragging, leftDragging;
    private final Point2D.Float worldMouse = new Point2D.Float();
    private final Point2D.Float screenMouse = new Point2D.Float();

    // Component dragging
    private Component<?>  dragged;
    private final Point2D.Float    dragOffset = new Point2D.Float();

    // Wire dragging
    DragSession<?> drag;

    // Wire drawing (click‑port → click‑other‑port)
    private Port<?>       wireStart; // null if not currently drawing

    // ---------------------------------------------------------------------
    // Construction
    // ---------------------------------------------------------------------

    public InputHandler(World world, OrthographicCamera camera, Renderer renderer) {
        this.world    = world;
        this.camera   = camera;
        this.renderer = renderer;
    }

    // ---------------------------------------------------------------------
    // Mouse events
    // ---------------------------------------------------------------------

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        updateMouse(screenX, screenY);

        switch (button) {
            case Input.Buttons.RIGHT:
                rightDragging = true;
                break;
            case Input.Buttons.LEFT:
                leftDragging = true;
                handleLeftClick(worldMouse);
                break;
        }
        return true;
    }



    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Point2D.Float worldPos = new Point2D.Float(worldMouse.x, worldMouse.y); // Keeps the old worldMouse for panning
        updateMouse(screenX, screenY);

        if (rightDragging) {
            // Pan camera.
            Point2D.Float delta = new Point2D.Float(worldMouse.x - worldPos.x, worldMouse.y - worldPos.y);
            camera.position.add(delta.x, delta.y, 0);
            camera.update();
            worldMouse.setLocation(worldPos);
            System.out.println("Right click dragged: " + dragged + "to " + worldPos);

        } else if (leftDragging && dragged != null) {
            // Move component following mouse.
            dragged.setPosition(worldPos.x - dragOffset.x, worldPos.y - dragOffset.y);
            System.out.println("dragged: " + dragged + "to " + worldPos);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        switch (button) {
            case Input.Buttons.RIGHT:
                rightDragging = false;
                System.out.println("Right click dragging ended.");
                break;
            case Input.Buttons.LEFT:
                if (dragged != null) {
                    // Drop component.
                    System.out.println("Left click dragging ended, dropped off component " + dragged + " at " + dragged.getPosition());
                    dragged = null;
                    leftDragging = false;
                } else if (wireStart != null) {
                    // Attempt to finish wire.
                    Point2D.Float worldPos = screenToWorld(screenX, screenY);
                    Port<?> end = findPortAt(worldPos);
                    if (end != null && end != wireStart) {
                        finishDrag(drag, end, world);

                    }
                    wireStart = null;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float zoomFactor = 1f + (amountY * 0.1f);
        camera.zoom = MathUtils.clamp(camera.zoom * zoomFactor, 0.05f, 2.5f);
        camera.update();
        return true;
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY) {
        updateMouse(screenX, screenY);
        return true;
    }

    // ---------------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------------

    /**
     * Updates the 2 private mouse position variables
     * @param x the X mouse coordinate on the screen
     * @param y the Y mouse coordinate on the screen
     */
    private void updateMouse(int x, int y) {
        screenMouse.setLocation(x, y);
        Point2D.Float worldPos = screenToWorld(x, y);
        System.out.println("Mouse clicked at: " + worldPos);
        worldMouse.setLocation(worldPos.x, worldPos.y);
    }

    /** Processes a primary‑button click in world space. */
    private void handleLeftClick(Point2D.Float worldPos) {
        Component<?> hit = findComponentAt(worldPos);
        if (hit != null) {
            System.out.println("Left clicked component: " + hit + " at " + hit.getPosition());
            // Start dragging the component (unless we clicked on a port).
            Port<?> port = hit.findPortAt(worldPos);
            if (port != null) {
                drag = new DragSession<>(port); // Begin wire
                System.out.println("Clicked port, so dragging port: " + drag);
            } else {
                dragged = hit;
                world.addComponent(dragged);
                dragOffset.setLocation(worldPos.x - hit.getPosition().x, worldPos.y - hit.getPosition().y);
            }
            world.setSelection(hit);
            return;
        }
        hit = findIconAt(worldPos);
        if (hit != null) {
            System.out.println("Left clicked icon: " + hit + " at " + hit.getPosition());
            dragged = hit.copy();
            world.addComponent(dragged);
            leftDragging = true;
        }

    }

    /** @return the world component under the cursor or {@code null} if none. */
    private Component<?> findComponentAt(Point2D.Float worldPos) {
        for (Component<?> c : world.getComponents()) {
            if (c.contains(worldPos)) {
                return c;
            }
        }
        return null;
    }

    private Component<?> findIconAt(Point2D.Float worldPos) {
        for (Component<?> c : world.getUnlocked()) {
            if (c.contains(worldPos)) {
                return c;
            }
        }
        return null;
    }

    /** @return port near the cursor or {@code null}. */
    private Port<?> findPortAt(Point2D.Float worldPos) {
        for (Component<?> c : world.getComponents()) {
            Port<?> p = c.findPortAt(worldPos);
            if (p != null) return p;
        }
        return null;
    }

    public Point2D.Float getMousePosition() {
        return worldMouse;
    }

    /** Converts a screen coordinate pair to world coordinates. */
    private Point2D.Float screenToWorld(int sx, int sy) {
        Vector3 v = camera.unproject(new Vector3(sx, sy, 0));
        return new Point2D.Float(v.x, v.y);
    }

    /** Generic helper – infers <T> from the first argument and
     guarantees the second argument has the same T. */
    static <T> void finishDrag(DragSession<T> drag, Port<?> endRaw, World world) {
        if (drag.start().compatibleWith(endRaw)) {   // run-time guard
            @SuppressWarnings("unchecked")
            Port<T> end = (Port<T>) endRaw;
            drag.finish(end, world);
        }
    }
}


/**
 * Helper that remembers the type of the port to prevent type erasure from requiring try-catch brackets when assigning
 * ports.
 *
 * @param <T> The type of the {@link Port }
 */
record DragSession<T>(Port<T> start) {

    void finish(Port<T> end, World world) {
        world.addCable(new Cable<>(start, end));
    }

}
