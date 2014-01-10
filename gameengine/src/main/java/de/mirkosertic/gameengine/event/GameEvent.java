package de.mirkosertic.gameengine.event;

public class GameEvent {

    public final String type;

    protected GameEvent(String aType) {
        type = aType;
    }
}