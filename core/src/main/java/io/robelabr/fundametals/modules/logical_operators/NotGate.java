package io.robelabr.fundametals.modules.logical_operators;

import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;

/**
 * A Not gate. You don't need an explanation for this right?
 * @author Robel Abraham
 */
public class NotGate extends Gate {
    @Override protected int maxInputs() { return 1; }
    @Override public boolean eval() { return !getInputs().get(0).getSignal(); }
    @Override
    public Component<Boolean> copy() {
        return new NotGate();
    }
    @Override
    public TinyVG getSprite() {
        return Assets.getInstance().get("gates/not_gate.tvg", TinyVG.class);
    }
    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/not_gate.tvg", TinyVG.class); }
}
