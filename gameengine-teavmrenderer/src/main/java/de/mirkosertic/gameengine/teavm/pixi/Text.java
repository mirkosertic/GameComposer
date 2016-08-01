package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;

public abstract class Text extends Sprite {

    @JSBody(params = {"aText"}, script = "return new PIXI.Text(aText);")
    public static native Text createText(String aText);

    @JSProperty
    public abstract Style getStyle();

    @JSProperty
    public abstract void setText(String aText);
}
