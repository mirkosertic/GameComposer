package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSProperty;

public abstract class Container extends DisplayObject {

    @JSProperty
    public abstract DisplayObject[] getChildren();

    public abstract void addChild(DisplayObject aChild);

    public abstract void removeChild(DisplayObject aChild);
}
