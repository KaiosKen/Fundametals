package io.robelabr.fundametals.modules.logical_operators;

import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.Port;
import io.robelabr.fundametals.modules.sources.SignalSource;

/**
 * Just the inverse of AND, nothing new to see
 * @author Robel Abraham
 */
public class NandGate extends Gate {

    @Override protected int maxInputs() { return -1; }   // unlimited
    @Override public boolean eval() {
        for (Port<Boolean> p : getInputs()) if (!p.getSignal()) return true;
        return false;
    }

    @Override
    public TinyVG getSprite() {
        return Assets.getInstance().get("gates/nand_gate.tvg", TinyVG.class);
    }
    @Override
    public Component<Boolean> copy() {
        return new NandGate();
    }

    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/nand_gate.tvg", TinyVG.class); }
}
