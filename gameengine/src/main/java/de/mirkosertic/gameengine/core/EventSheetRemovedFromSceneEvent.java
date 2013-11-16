package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class EventSheetRemovedFromSceneEvent extends GameEvent {

    private final ReadOnlyProperty<EventSheet> eventSheet;

    public EventSheetRemovedFromSceneEvent(EventSheet aEventSheet) {
        super("EventSheetAddedToSceneEvent");
        eventSheet = registerProperty(new ReadOnlyProperty<EventSheet>(this, "eventSheet", aEventSheet));
    }

    public ReadOnlyProperty<EventSheet> eventSheetProperty() {
        return eventSheet;
    }
}