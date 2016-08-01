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
}