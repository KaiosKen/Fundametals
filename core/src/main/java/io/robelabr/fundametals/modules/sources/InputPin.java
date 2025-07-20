package io.robelabr.fundametals.modules.sources;


import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;
import io.robelabr.fundametals.modules.logical_operators.AndGate;

/**
 *  A mutable input pin you can toggle at runtime (e.g. a switch).
 * @author Robel Abraham
 * */
public class InputPin extends Component<Boolean> implements SignalSource {

    public InputPin() {
        super();
        output = false;
    }

    @Override
    public TinyVG getSprite() {
        if (output) {
            return Assets.getInstance().get("inputs/button_pressed.tvg", TinyVG.class);
        }
        return Assets.getInstance().get("inputs/button_unpressed.tvg", TinyVG.class);
    }
    @Override
    public Component<Boolean> copy() {
        return new InputPin();
    }

    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("inputs/button_unpressed.tvg", TinyVG.class); }

    @Override
    public void draw(TinyVGShapeDrawer drawer, float zoom) {
        getSprite().setPosition(pos.x, pos.y);
        getSprite().draw(drawer);
    }

    public void set(boolean value) { output = value; }
    @Override public boolean eval()  { return output; }
}
