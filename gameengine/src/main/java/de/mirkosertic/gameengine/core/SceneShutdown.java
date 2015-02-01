package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class SceneShutdown extends GameEvent {

    public SceneShutdown() {
        super("GameShutdown");
    }
}
