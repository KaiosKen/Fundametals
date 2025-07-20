package io.robelabr.fundametals.modules.displays;

import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Component;

public class LightBulb extends Component<Boolean> {

    public LightBulb() {
        super();
        output = false;
    }

    @Override
    public TinyVG getSprite() {
        return (output) ? Assets.getInstance().get("outputs/light_on.tvg", TinyVG.class) : Assets.getInstance().get("outputs/light_off.tvg", TinyVG.class);
    }
    /** {@link TinyVG } icon (used in the palette) – cached per gate type. For now, icon is the same as component */
    public static TinyVG getIcon() { return Assets.getInstance().get("gates/buffer.tvg", TinyVG.class); }
    @Override
    public Component<Boolean> copy() {
        return new LightBulb();
    }
    @Override
    public void draw(TinyVGShapeDrawer drawer, float zoom) {
        getSprite().setPosition(pos.x, pos.y);
        getSprite().draw(drawer);
    }
}
