package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;

public abstract class Container extends DisplayObject {

    @JSProperty
    public abstract DisplayObject[] getChildren();

    public abstract void addChild(DisplayObject aChild);

    public abstract void removeChild(DisplayObject aChild);

    @JSBody(params = {"aChild1", "aChild2"}, script = "this.swapChildren(aChild1, aChild2);")
    public abstract void swapChildren(DisplayObject aChild1, DisplayObject aChild2);
}
