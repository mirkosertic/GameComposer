package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class EventSheetRemovedFromSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<EventSheetRemovedFromScene, de.mirkosertic.gameengine.core.EventSheet> EVENTSHEET = new Field<EventSheetRemovedFromScene, de.mirkosertic.gameengine.core.EventSheet>("eventSheet", de.mirkosertic.gameengine.core.EventSheet.class) {
    @Override
    public de.mirkosertic.gameengine.core.EventSheet getValue(EventSheetRemovedFromScene aObject) {
      return aObject.eventSheet;
    }
  };

  public EventSheetRemovedFromSceneClassInformation() {
    register(EVENTSHEET);
  }
}
