package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.*;

public class GameObjectInstanceAddedToSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<GameObjectInstanceAddedToScene, de.mirkosertic.gameengine.core.GameObjectInstance> INSTANCE = new Field<GameObjectInstanceAddedToScene, de.mirkosertic.gameengine.core.GameObjectInstance>("instance", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(GameObjectInstanceAddedToScene aObject) {
      return aObject.instance;
    }
  };

  public GameObjectInstanceAddedToSceneClassInformation() {
    register(INSTANCE);
  }
}
