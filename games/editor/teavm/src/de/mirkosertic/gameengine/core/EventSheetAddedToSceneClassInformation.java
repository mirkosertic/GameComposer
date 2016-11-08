package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class EventSheetAddedToSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<EventSheetAddedToScene, de.mirkosertic.gameengine.core.EventSheet> EVENTSHEET = new Field<EventSheetAddedToScene, de.mirkosertic.gameengine.core.EventSheet>("eventSheet", de.mirkosertic.gameengine.core.EventSheet.class) {
    @Override
    public de.mirkosertic.gameengine.core.EventSheet getValue(EventSheetAddedToScene aObject) {
      return aObject.eventSheet;
    }
  };

  public EventSheetAddedToSceneClassInformation() {
    register(EVENTSHEET);
  }
}
