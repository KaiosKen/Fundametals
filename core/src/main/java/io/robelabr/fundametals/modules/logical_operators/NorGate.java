package io.robelabr.fundametals.modules.logical_operators;

import dev.lyze.gdxtinyvg.TinyVG;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.Port;

/**
 * Nor Gate!!!!!!!
 * @author Robel Abraham
 */
public class NorGate extends Gate {

    @Override
    protected int maxInputs() {
        return -1; // Unlimited Inputs
    }

    @Override
    public boolean eval() {
        for (Port<Boolean> port : ports) {
            if (port.getSignal()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public Component<Boolean> copy() {
        return new NorGate();
    }
    @Override
    public TinyVG getSprite() {
        return Assets.getInstance().get("gates/nor_gate.tvg", TinyVG.class);
    }

    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/nor_gate.tvg", TinyVG.class); }
}
