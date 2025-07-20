package io.robelabr.fundametals.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;

/**
 * Singleton wrapper around LibGDX's {@link AssetManager}.
 * <p>
 * Handles loading/unloading of textures, atlases, fonts, sounds, etc.
 * @author Robel Abraham
 */
public final class Assets {

    // ---------------------------------------------------------------------
    // Static singleton boilerplate
    // ---------------------------------------------------------------------
    private static final Assets INSTANCE = new Assets();
    public static Assets getInstance() { return INSTANCE; }
    private Assets() { }

    // ---------------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------------
    private final AssetManager manager = new AssetManager();

    /**
     * Queue all assets. Call once from {@code Main.create()}.
     */
    public void loadAll() {
        FileHandleResolver resolver = new InternalFileHandleResolver();

        // 👇 Register the loader for BOTH “TinyVG.class” and the “.tvg” extension, or else a "No loader for type: TinyVG" will happen.
        manager.setLoader(TinyVG.class, new TinyVGAssetLoader(resolver));
        manager.setLoader(TinyVG.class, ".tvg", new TinyVGAssetLoader(resolver));

        // Logic Gates
        manager.load("gates/and_gate.tvg", TinyVG.class);
        manager.load("gates/or_gate.tvg", TinyVG.class);
        manager.load("gates/not_gate.tvg", TinyVG.class);
        manager.load("gates/nand_gate.tvg", TinyVG.class);
        manager.load("gates/nor_gate.tvg", TinyVG.class);
        manager.load("gates/xor_gate.tvg", TinyVG.class);
        manager.load("gates/xnor_gate.tvg", TinyVG.class);
        manager.load("gates/buffer.tvg", TinyVG.class);
        manager.load("gates/constant_low.tvg", TinyVG.class);
        manager.load("gates/constant_high.tvg", TinyVG.class);
        // Inputs
        manager.load("inputs/button_unpressed.tvg", TinyVG.class);
        manager.load("inputs/button_pressed.tvg", TinyVG.class);
        manager.load("inputs/port.tvg", TinyVG.class);
        // Outputs
        manager.load("outputs/light_on.tvg", TinyVG.class);
        manager.load("outputs/light_off.tvg", TinyVG.class);

        // Defaults
        manager.load("defaults/question_mark.tvg", TinyVG.class);


    }

    /**
     * Fetches the preloaded asset and returns it
     * @param fileName The location of the file fetching
     * @param type The class type of the file (TinyVG, TextureAtlas, etc;)
     * @return The asset fetched
     * @param <T> The type of asset you're getting.
     */
    public <T> T get(String fileName, Class<T> type) {
        return manager.get(fileName, type);
    }

    /**
     * Blocks until every queued asset is finished loading.
     * Call after {@link #loadAll()} and before you render the first frame.
     */
    public void finishLoading() {
        manager.finishLoading();

    }

    /** Dispose everything on game shutdown. */
    public void dispose() {
        manager.dispose();
    }


}
