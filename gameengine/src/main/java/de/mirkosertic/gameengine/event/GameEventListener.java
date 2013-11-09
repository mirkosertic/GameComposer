package de.mirkosertic.gameengine.event;

public interface GameEventListener<T extends GameEvent> {

    void handleGameEvent(T aEvent);
}
