package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

public abstract class Texture implements JSObject {

    @JSBody(params = {"aResourceName"}, script = "return PIXI.Texture.fromImage(aResourceName);")
    public static native Texture createTextureFromImage(String aResourceName);

    public abstract void destroy();
}
