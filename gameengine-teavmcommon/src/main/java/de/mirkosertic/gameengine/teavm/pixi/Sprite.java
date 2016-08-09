package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;

public abstract class Sprite extends DisplayObject {

    @JSBody(params = {"aTexture"}, script = "return new PIXI.Sprite(aTexture);")
    public static native Sprite createSprite(Texture aTexture);

    @JSProperty
    public abstract Point getAnchor();
}
