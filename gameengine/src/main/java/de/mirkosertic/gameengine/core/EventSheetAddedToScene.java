package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class EventSheetAddedToScene extends GameEvent {

    public final EventSheet eventSheet;

    public EventSheetAddedToScene(EventSheet aEventSheet) {
        super("EventSheetAddedToScene");
        eventSheet = aEventSheet;
    }
}