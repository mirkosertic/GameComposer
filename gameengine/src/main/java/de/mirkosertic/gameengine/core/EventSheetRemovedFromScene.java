package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class EventSheetRemovedFromScene extends GameEvent {

    private final Property<EventSheet> eventSheet;

    public EventSheetRemovedFromScene(EventSheet aEventSheet) {
        super("EventSheetRemovedFromScene");
        eventSheet = registerProperty(new Property<EventSheet>(this, "eventSheet", aEventSheet));
    }

    public ReadOnlyProperty<EventSheet> eventSheetProperty() {
        return eventSheet;
    }
}