package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.type.*;

public class PlayerScoreClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.playerscore.PlayerScore> DELETE = new Method<de.mirkosertic.gameengine.playerscore.PlayerScore>("delete", void.class, new Class[] {}) {
    @Override
    public Object invoke(PlayerScore aObject, Object[] aArguments) {
      aObject.delete();
      return null;
    }
  };

  public static final Field<PlayerScore, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.ScoreValue>> SCOREVALUEPROPERTY = new Field<PlayerScore, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.ScoreValue>>("scoreValueProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.ScoreValue> getValue(PlayerScore aObject) {
      return aObject.scoreValueProperty();
    }
  };

  public PlayerScoreClassInformation() {
    register(DELETE);
    register(SCOREVALUEPROPERTY);
  }
}
