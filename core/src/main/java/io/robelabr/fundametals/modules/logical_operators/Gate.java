package io.robelabr.fundametals.modules.logical_operators;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.Direction;
import io.robelabr.fundametals.modules.Port;
import io.robelabr.fundametals.modules.sources.SignalSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for all the types of gates the player can make and use.
    AND: Outputs 1 (true) only if all inputs are 1.
    OR: Outputs 1 (true) if at least one input is 1.
    NOT: Outputs the inverse of the input (1 becomes 0, 0 becomes 1).
    NAND: Outputs the inverse of the AND gate (1 becomes 0, 0 becomes 1).
    NOR: Outputs the inverse of the OR gate (1 becomes 0, 0 becomes 1).
    XOR: Outputs 1 (true) if the inputs are different.
    XNOR: Outputs 1 (true) if the inputs are the same.

 @author Robel Abraham
 */
public abstract class Gate extends Component<Boolean> implements SignalSource {


    /**
     * Super constructor for the gates, opening the amount of ports passed
     */
    protected Gate() {
        super();
        for (int i = 0; i < maxInputs(); i++) {
            ports.add(new Port<Boolean>(this.pos, Direction.WEST, Boolean.class));
        }
    }

    /**
     * Adds a component that outputs a boolean value as an input to the gate
     * @param port The Port that the input is originating from
     */
    public void add(Port<Boolean> port) {
        int cap = maxInputs();
        if (cap != -1 && ports.size() >= cap)
            throw new IllegalStateException(
                getClass().getSimpleName() + " accepts at most " + cap + " input(s).");
        ports.add(port);
    }

    /**
     * Sets the limit of ports for the gate. Varies depending on the type.
     * @return a inclusive integer limit
     */
    protected abstract int maxInputs();
    /**
     * Returns the list of {@link Port } inputs
     * @return a List<SignalSource>
     */
    protected List<Port<Boolean>> getInputs() { return ports; }

    public int inputSize() { return ports.size(); }


    /** {@link TinyVG } icon (used in the palette) – cached per gate type. */
    public static TinyVG getIcon() { return Assets.getInstance().get("defaults/question_mark.tvg", TinyVG.class); }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + inputSize() + ")";
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, float zoom) {
        getSprite().setPosition(pos.x, pos.y);
        getSprite().draw(drawer);
    }
}
