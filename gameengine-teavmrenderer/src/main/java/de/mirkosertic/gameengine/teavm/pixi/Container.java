package de.mirkosertic.gameengine.teavm.pixi;

public abstract class Container extends DisplayObject {

    public abstract void addChild(DisplayObject aChild);

    public abstract void removeChild(DisplayObject aChild);
}
