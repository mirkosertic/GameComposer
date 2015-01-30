package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;

@InheritedClassInformation
public class EventSheetRemovedFromScene extends GameEvent {

    private static final EventSheetRemovedFromSceneClassInformation CIINSTANCE = new EventSheetRemovedFromSceneClassInformation();

    @ReflectiveField
    public final EventSheet eventSheet;

    public EventSheetRemovedFromScene(EventSheet aEventSheet) {
        super("EventSheetRemovedFromScene");
        eventSheet = aEventSheet;
    }

    @Override
    public EventSheetRemovedFromSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}