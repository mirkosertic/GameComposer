package de.mirkosertic.gameengine.core;

public interface GameEventListener<T extends GameEvent> {

    void handleGameEvent(T aEvent);
}
