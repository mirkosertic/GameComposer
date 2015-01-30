package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

@InheritedClassInformation
public class EventSheetAddedToScene extends GameEvent {

    private static final EventSheetAddedToSceneClassInformation CIINSTANCE = new EventSheetAddedToSceneClassInformation();

    @ReflectiveField
    public final EventSheet eventSheet;

    public EventSheetAddedToScene(EventSheet aEventSheet) {
        super("EventSheetAddedToScene");
        eventSheet = aEventSheet;
    }

    @Override
    public EventSheetAddedToSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}