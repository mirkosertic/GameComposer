package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameShutdownEvent extends GameEvent {

    public GameShutdownEvent() {
        super("GameShutdownEvent");
    }
}
