package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameObjectConfigurationChangedClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<GameObjectConfigurationChanged, de.mirkosertic.gameengine.core.GameObject> OBJECT = new Field<GameObjectConfigurationChanged, de.mirkosertic.gameengine.core.GameObject>("object", de.mirkosertic.gameengine.core.GameObject.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObject getValue(GameObjectConfigurationChanged aObject) {
      return aObject.object;
    }
  };

  public GameObjectConfigurationChangedClassInformation() {
    register(OBJECT);
  }
}
