package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.Size;

public class SetScreenResolution extends GameEvent {

    public final Size screenSize;

    public SetScreenResolution(Size aSize) {
        super("SetScreenResolution");
        screenSize = aSize;
    }
}