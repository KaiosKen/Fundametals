package io.robelabr.fundametals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import io.robelabr.fundametals.graphics.Assets;
import io.robelabr.fundametals.modules.Cable;
import io.robelabr.fundametals.modules.Component;

import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * <p><strong>Renderer</strong> translates the abstract simulation held by
 * {@link World} into pixels.  It maintains a single orthographic camera that
 * supports pan and zoom.  When the zoom level crosses
 * {@link #ICON_ZOOM_THRESHOLD} the renderer seamlessly switches between the
 * high‑level ‘icon’ view and the low‑level schematic view that shows every
 * individual transistor / wire inside a gate.</p>
 *
 * <p>The class also draws a fixed‑width sidebar on the right‑hand side that
 * lists unlocked components as draggable icons.</p>
 */
public class Renderer {

    // ---------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------

    /** World zoom greater than this → draw icons; smaller → draw internals. */
    private static final float ICON_ZOOM_THRESHOLD = 0.35f;

    /** Width (in world units) of the right‑hand side toolbox. */
    private static final float TOOLBOX_WIDTH = 320f;

    // ---------------------------------------------------------------------
    // Rendering aides
    // ---------------------------------------------------------------------

    private final World world;
    private final OrthographicCamera worldCamera;
    private final OrthographicCamera uiCamera;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final TinyVGShapeDrawer drawer;
    private final BitmapFont font;

    // ---------------------------------------------------------------------
    // Construction & lifecycle
    // ---------------------------------------------------------------------

    public Renderer(World world,
                    OrthographicCamera worldCamera,
                    OrthographicCamera uiCamera,
                    SpriteBatch batch,
                    ShapeRenderer shapes,
                    TinyVGShapeDrawer drawer) {
        this.world   = world;
        this.worldCamera = worldCamera;
        this.uiCamera = uiCamera;
        this.batch   = batch;
        this.shapes  = shapes;
        this.drawer    = drawer;
        this.font     = new BitmapFont();
    }

    /**
     * Renders both the sandbox play‑area and the right‑hand toolbox.  Call once
     * per frame from <code>Main.render()</code>.
     */
    public void render(float dt) {
        worldCamera.update();

        // World (use camera matrices)
        batch.setProjectionMatrix(worldCamera.combined);
        shapes.setProjectionMatrix(worldCamera.combined);


        drawComponents();

        // --- UI PASS ---
        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);
        shapes.setProjectionMatrix(uiCamera.combined);
        drawText();
        drawToolbox();
    }

    /** Resize callback forwards new back‑buffer dimensions to TinyVG. */
    public void resize(int width, int height) {
        // World camera: keep same logical size, but adjust aspect
        worldCamera.viewportWidth  = width  * worldCamera.zoom;
        worldCamera.viewportHeight = height * worldCamera.zoom;
        worldCamera.update();

        // UI camera: lock to pixel dimension
        uiCamera.setToOrtho(false, width, height);
        uiCamera.update();
    }

    /** Disposes owned resources.  Call from <code>Main.dispose()</code>. */
    public void dispose() {
        batch.dispose();
        shapes.dispose();
        drawer.dispose();
        // TinyVG uses shared static resources – no explicit dispose needed.
    }

    // ---------------------------------------------------------------------
    // Internal helpers
    // ---------------------------------------------------------------------


    private void drawText() {
        batch.begin();
        font.draw(batch, "Mouse world pos: ", 50, Gdx.graphics.getHeight() - 50);
        font.draw(batch, "Mouse screen pos: ", 50, Gdx.graphics.getHeight() - 70);

        batch.end();
    }

    /** Draws either icons or detailed sub‑components depending on zoom. */
    private void drawComponents() {
        batch.begin();
        // boolean iconMode = worldCamera.zoom > ICON_ZOOM_THRESHOLD;
        boolean iconMode = false;
        for (Component<?> c : world.getComponents()) {
            if (iconMode) {
                drawIcon(c);
            } else {
                c.draw(drawer, worldCamera.zoom); // Let each component render itself.
            }
        }

        // Draw cables on top so they are visible over metal layers.
        for (Cable<?> cable : world.getCables()) {
            cable.draw(drawer);
        }
        batch.end();

    }

    /** Draws a single component icon centered on its logical position. UNUSED */
    private void drawIcon(Component<?> c) {
        Point2D.Float pos = c.getPosition();
        float   sz  = c.getSprite().getScaledWidth() * c.getSprite().getScaledHeight();

        batch.end();
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.2f, 0.24f, 0.3f, 1f);
        shapes.rect(pos.x - sz / 2, pos.y - sz / 2, sz, sz);
        shapes.end();
        batch.begin();

        c.setPosition(pos.x - sz / 2, pos.y - sz / 2);
        c.draw(drawer, 1);
    }

    /** Draws the toolbox that lists unlocked components for drag‑placement. Uses the UI Camera */
    private void drawToolbox() {

        // 1) Constants in pixels
        final float PANEL_W       = 320f;
        final float PAD_PX_MAX    = 16f;   // start with 16px gap
        final float PAD_PX_MIN    = 4f;    // never go below 4px
        final float ICON_PX_MIN   = 24f;   // don't shrink icons smaller than 24px tall

        // pixel coords: (0,0)=bottom-left, (W,H)=top-right
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // Background panel
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.15f,0.17f,0.19f,1f);
        shapes.rect(screenW - PANEL_W, 0, PANEL_W, screenH);
        shapes.end();

        batch.begin();

        // Icons
        Array<Component<?>> types = world.getUnlocked();
        int   nIcons      = types.size;
        float availH      = screenH;                  // full panel height
        float iconWMax    = PANEL_W - 2*PAD_PX_MAX;   // max icon width inside panel
        float iconHMax    = iconWMax;                 // we’ll fit a square
        if (nIcons == 0) return;
        // Try full-size icons + variable padding
        float padPx = Math.min(
            PAD_PX_MAX,
            (availH - nIcons * iconHMax) / (nIcons + 1)
        );
        float iconPx = iconHMax;
        // If padding too small, clamp and shrink icons
        if (padPx < PAD_PX_MIN) {
            padPx = PAD_PX_MIN;
            iconPx = (availH - (nIcons + 1)*padPx) / nIcons;
            iconPx = Math.max(iconPx, ICON_PX_MIN);
        }


        // Draw each icon at the computed size
        float x = screenW - PANEL_W + padPx;         // left padding inside panel
        for (int i = 0; i < nIcons; i++) {
           TinyVG tvg;

            tvg = types.get(i).getSprite();

            // uniform scale to fit exactly ICON_PX tall
            float scale = iconPx / tvg.getUnscaledHeight();

            // compute bottom‐left Y so the TOP of the box is:
            //   screenH - PAD_PX - (ICON_PX + PAD_PX)*i
            float yTop    = screenH - padPx - (iconPx + padPx) * i;
            float yBottom = yTop - iconPx;

            // position & draw
            tvg.setScale(scale, scale);
            tvg.setPosition(x, yBottom);
            tvg.draw(drawer);
        }
        batch.end();
    }

    /** @return the shared orthographic camera for panning & zooming. */
    public OrthographicCamera getWorldCamera() {
        return worldCamera;
    }
}
