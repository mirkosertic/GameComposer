package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class Point implements JSObject {

    public abstract void set(float aX, float aY);

    @JSProperty
    public abstract void setX(float aX);

    @JSProperty
    public abstract void setY(float aY);
}
