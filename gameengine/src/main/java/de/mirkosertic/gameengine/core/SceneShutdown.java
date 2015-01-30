package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

public class SceneShutdown extends GameEvent {

    public SceneShutdown() {
        super("GameShutdown");
    }
}
