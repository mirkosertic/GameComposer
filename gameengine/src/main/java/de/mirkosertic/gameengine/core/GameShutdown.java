package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameShutdown extends GameEvent {

    public GameShutdown() {
        super("GameShutdown");
    }
}
