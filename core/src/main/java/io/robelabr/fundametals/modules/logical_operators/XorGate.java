package io.robelabr.fundametals.modules.logical_operators;

import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.Port;

/**
 * Xor gate. TODO: Max of 4 inputs because that's how it's traditionally done in silicon. To use more than 4 inputs, you must chain them
 * @author Robel Abraham
 */
public class XorGate extends Gate {

    // 4 is the max to mimic hardware limitations
    @Override protected int maxInputs() { return 4; }

    @Override public boolean eval() {
        /*
        XOR-assignment on a boolean → xor = xor ^ s.eval()
        • Starts with xor == false.
        • For each input:
         - If s.eval() is true  ⇒ xor flips (false→true or true→false).
         - If s.eval() is false ⇒ xor stays the same.
        • After the loop, xor is true **iff an odd number of inputs were true**,
        which is exactly the definition of a multi-input XOR.
            */
        boolean xor = false;
        for (Port<Boolean> p : getInputs()) xor ^= p.getSignal();
        return xor;
    }
    @Override
    public Component<Boolean> copy() {
        return new XorGate();
    }

    @Override
    public TinyVG getSprite() {
        return Assets.getInstance().get("gates/xor_gate.tvg", TinyVG.class);
    }

    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/xor_gate.tvg", TinyVG.class); }
}
