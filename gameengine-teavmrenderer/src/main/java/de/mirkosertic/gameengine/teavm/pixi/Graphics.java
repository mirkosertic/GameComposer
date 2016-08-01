package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;

public abstract class Graphics extends Container {

    @JSBody(params = {}, script = "return new PIXI.Graphics();")
    public static native Graphics createGraphics();

    @JSProperty
    public abstract void setWidth(int aWidth);

    @JSProperty
    public abstract void setHeight(int aHeight);

    public abstract void moveTo(int aX, int aY);

    public abstract void lineTo(int aX, int aY);

    public abstract void beginFill(int aColor);

    public abstract void endFill();

    public abstract void drawRect(int aX, int aY, int aWidth, int aHeight);

    public abstract void clear();

    public abstract void lineStyle(int aLineWidth, int aColor, float aAlpha);

    public abstract Texture generateTexture(int aResolution, int aScaleMode);
}