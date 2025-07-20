package io.robelabr.fundametals;

import com.badlogic.gdx.utils.Array;
import io.robelabr.fundametals.modules.Cable;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.displays.LightBulb;
import io.robelabr.fundametals.modules.logical_operators.*;
import io.robelabr.fundametals.modules.sources.ConstantGate;
import io.robelabr.fundametals.modules.sources.InputPin;

/**
 * <p><strong>World</strong> owns the simulation state: all placed components,
 * wires connecting them, unlocked recipes, and the current selection.  It
 * deliberately contains <em>no rendering logic</em>.  The class exposes an API
 * that higher‑level systems (renderer, input, save‑load) can call.</p>
 */
public class World {

    // ---------------------------------------------------------------------
    // Data collections
    // ---------------------------------------------------------------------

    private final Array<Component<?>> components = new Array<>();
    private final Array<Cable<?>> cables = new Array<>();

    /** Component types that the player has unlocked & can drag from the UI. */
    private final Array<Component<?>> unlocked = new Array<>();

    /** Currently hovered or selected component (maybe {@code null}). */
    private Component<?> selection;

    // ---------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------

    /**
     * Advances the simulation by <var>dt</var> seconds.
     *
     * @param dt Fixed or variable time‑step from the render loop.
     */
    public void update(float dt) {
        // Step each tickable component (e.g. clock, constant source, debouncer).
        for (Component<?> c : components) {
            c.update(dt);
        }
    }

    /** @return all components currently placed in the world. */
    public Array<Component<?>> getComponents() {
        return components;
    }

    /** @return all wires currently connecting ports. */
    public Array<Cable<?>> getCables() {
        return cables;
    }

    /** @return the component the user last clicked or hovered. */
    public Component<?> getSelection() {
        return selection;
    }

    /** Sets the current selection (called by {@link InputHandler}). */
    public void setSelection(Component<?> c) {
        this.selection = c;
    }

    /** Adds a component instance to the world at the given location. */
    public <T extends Component<?>> void addComponent(T component) {
        components.add(component);
    }

    /** Removes a component & any wires attached to its ports. */
    public void removeComponent(Component<?> component) {
        components.removeValue(component, true);
        // TODO Remove linked wires – requires Port bookkeeping.
    }

    /** Adds a new wire to the simulation. */
    public void addCable(Cable<?> cable) {
        cables.add(cable);
    }

    /**
     * Marks a component type as unlocked so it appears in the right‑hand side
     * toolbar.  Called automatically when the player first crafts / places an
     * instance, but can also be triggered by research events or cheats.
     */
    public <T extends Component<?>> void unlock(T type) {
        if (!unlocked.contains(type, false)) {
            unlocked.add(type);
        }
    }

    /** @return immutable view of unlocked component recipes. */
    public Array<Component<?>> getUnlocked() {
        return unlocked;
    }

    // ---------------------------------------------------------------------
    //  Boxing utilities (player‑defined components)
    // ---------------------------------------------------------------------

    /**
     * Creates a new composite component by covering a selection of parts with a
     * user‑defined name (i.e. player‑boxing).  For brevity this stub just marks
     * all selected components as hidden & returns <code>null</code> – wire up
     * your own <code>CompositeComponent</code> subclass for full behaviour.
     */
    public Component<?> box(String name, Array<Component<?>> parts) {
        // TODO Implement composite creation & port mapping.
        return null;
    }

    public void unlockAll() {
        unlocked.clear();
        unlock(new ConstantGate(true));
        unlock(new InputPin());
        unlock(new LightBulb());
        unlock(new AndGate());
        unlock(new OrGate());
        unlock(new NandGate());
        unlock(new NorGate());
        unlock(new NotGate());
        unlock(new XorGate());
        unlock(new XnorGate());
    }
}
