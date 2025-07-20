package io.robelabr.fundametals.modules.logical_operators;

import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.Port;

/**
 *  Any input size AND (true only when *all* inputs are true, any size ≥1).
 * @author Robel Abraham
 */
public class AndGate extends Gate {

    @Override protected int maxInputs() { return -1; }

    @Override public boolean eval() {
        for (Port<Boolean> s : getInputs()) if (!s.getSignal()) return false;
        return true;                         // vacuous true on 0 inputs
    }

    @Override
    public TinyVG getSprite() {
        return Assets.getInstance().get("gates/and_gate.tvg", TinyVG.class);
    }

    @Override
    public Component<Boolean> copy() {
        return new AndGate();
    }

    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/and_gate.tvg", TinyVG.class); }
}
