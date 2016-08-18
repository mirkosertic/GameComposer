package de.mirkosertic.gameengine.type;

public class TouchPosition {

    public final TouchIdentifier identifier;
    public final int x;
    public final int y;

    public TouchPosition(TouchIdentifier aIdentifier, int aX, int aY) {
        identifier = aIdentifier;
        x = aX;
        y = aY;
    }
}
