package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameEventClassInformation extends ClassInformation {

  public final Field<GameEvent, String> TYPE = new Field<GameEvent, String>("type", String.class) {
    @Override
    public String getValue(GameEvent aObject) {
      return aObject.type;
    }
  };

  public GameEventClassInformation() {
    register(TYPE);
  }
}
