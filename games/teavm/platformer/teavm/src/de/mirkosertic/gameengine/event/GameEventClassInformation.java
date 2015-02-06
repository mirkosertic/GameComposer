package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.type.*;

public class GameEventClassInformation extends ClassInformation {

  public static final Field<GameEvent, String> TYPE = new Field<GameEvent, String>("type", String.class) {
    @Override
    public String getValue(GameEvent aObject) {
      return aObject.type;
    }
  };

  public GameEventClassInformation() {
    register(TYPE);
  }
}
