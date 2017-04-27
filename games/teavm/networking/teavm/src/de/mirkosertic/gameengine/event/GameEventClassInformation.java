package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameEventClassInformation extends ClassInformation {

  public final Field<GameEvent, de.mirkosertic.gameengine.event.GameEventType> TYPE = new Field<GameEvent, de.mirkosertic.gameengine.event.GameEventType>("type", de.mirkosertic.gameengine.event.GameEventType.class) {
    @Override
    public de.mirkosertic.gameengine.event.GameEventType getValue(GameEvent aObject) {
      return aObject.type;
    }
  };

  public GameEventClassInformation() {
    register(TYPE);
  }
}
