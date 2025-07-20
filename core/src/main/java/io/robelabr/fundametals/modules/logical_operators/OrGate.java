package io.robelabr.fundametals.modules.logical_operators;

import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.Port;

/**
 * Any (that's right any) size input OR (true when any (that's right any) input is true).
 * @author Robel Abraham
 */
public class OrGate extends Gate {
    @Override protected int maxInputs() { return -1; }
    @Override public boolean eval() {
        for (Port<Boolean> p : getInputs()) if (p.getSignal()) return true;
        return false;
    }
    @Override
    public Component<Boolean> copy() {
        return new OrGate();
    }

    @Override
    public TinyVG getSprite() {
        return Assets.getInstance().get("gates/or_gate.tvg", TinyVG.class);
    }

    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/or_gate.tvg", TinyVG.class); }
}
