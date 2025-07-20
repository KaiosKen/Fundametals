package io.robelabr.fundametals.modules.sources;

import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.logical_operators.AndGate;
import io.robelabr.fundametals.modules.logical_operators.Gate;

/**
 * Gate that returns the value it was assigned on creation
 */
public class ConstantGate extends Gate {
    public ConstantGate(boolean value) { this.output = value; }

    @Override
    protected int maxInputs() {
        return -1;
    }

    @Override public boolean eval() { return output; }

    @Override
    public Component<Boolean> copy() {
        return new ConstantGate(output);
    }
    @Override
    public TinyVG getSprite() {
        return (output) ? Assets.getInstance().get("gates/constant_high.tvg", TinyVG.class) : Assets.getInstance().get("gates/constant_low.tvg", TinyVG.class);
    }

    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/constant_high.tvg", TinyVG.class); }
}
