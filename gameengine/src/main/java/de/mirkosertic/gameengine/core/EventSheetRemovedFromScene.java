package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class EventSheetRemovedFromScene extends GameEvent {

    public final EventSheet eventSheet;

    public EventSheetRemovedFromScene(EventSheet aEventSheet) {
        super("EventSheetRemovedFromScene");
        eventSheet = aEventSheet;
    }
}