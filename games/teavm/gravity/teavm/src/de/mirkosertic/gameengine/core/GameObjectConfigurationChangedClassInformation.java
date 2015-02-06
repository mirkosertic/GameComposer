package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.*;

public class GameObjectConfigurationChangedClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<GameObjectConfigurationChanged, de.mirkosertic.gameengine.core.GameObject> OBJECT = new Field<GameObjectConfigurationChanged, de.mirkosertic.gameengine.core.GameObject>("object", de.mirkosertic.gameengine.core.GameObject.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObject getValue(GameObjectConfigurationChanged aObject) {
      return aObject.object;
    }
  };

  public GameObjectConfigurationChangedClassInformation() {
    register(OBJECT);
  }
}
