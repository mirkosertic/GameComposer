package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.EventSheet;

public class EventSheetSelectedEvent {

    private final EventSheet eventSheet;

    public EventSheetSelectedEvent(EventSheet aSheet) {
        eventSheet = aSheet;
    }

    public EventSheet getEventSheet() {
        return eventSheet;
    }
}
