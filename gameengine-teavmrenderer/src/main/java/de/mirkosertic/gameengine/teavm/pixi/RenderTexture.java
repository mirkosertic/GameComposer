package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;

public abstract class RenderTexture extends Texture {

    @JSBody(params = {"aRenderer", "aWidth", "aHeight"}, script = "return PIXI.RenderTexture(aRenderer, aWidth, aHeight);")
    public static native RenderTexture createRenderTexture(Renderer aRenderer, int aWidth, int aHeight);

    public abstract void render(DisplayObject aObject);
}
