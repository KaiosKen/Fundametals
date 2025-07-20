package io.robelabr.fundametals.modules.logical_operators;

import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.Port;

/**
 * Gate where all inputs must match. Funny name
 * @author Robel Abraham
 */
public class XnorGate extends Gate {
    @Override protected int maxInputs() { return -1; }


    @Override public boolean eval() {
        // Takes the inverse of the xor operation, if this confuses you check out XorGate.eval().
        boolean xor = false;
        for (Port<Boolean> p : ports) xor ^= p.getSignal();
        return !xor;
    }
    @Override
    public Component<Boolean> copy() {
        return new XnorGate();
    }
    @Override
    public TinyVG getSprite() {
        return Assets.getInstance().get("gates/xnor_gate.tvg", TinyVG.class);
    }

    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/xnor_gate.tvg", TinyVG.class); }
}
