package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class DisplayObject implements JSObject {

    public abstract void destroy();

    @JSProperty
    public abstract Point getPosition();

    @JSProperty
    public abstract Point getScale();

    @JSProperty
    public abstract void setRotation(float aRotationInRadians);

    @JSProperty
    public abstract Point getPivot();

    @JSProperty
    public abstract void setWidth(int aWidth);

    @JSProperty
    public abstract void setHeight(int aHeight);

    @JSProperty
    public abstract void setZOrder(int aIndex);

    @JSProperty
    public abstract int getZOrder();

    @JSProperty
    public abstract void setAlpha(float alpha);
}