package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.*;

public class GameObjectCollisionClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<GameObjectCollision, de.mirkosertic.gameengine.core.GameObjectInstance> INSTANCE1 = new Field<GameObjectCollision, de.mirkosertic.gameengine.core.GameObjectInstance>("instance1", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(GameObjectCollision aObject) {
      return aObject.instance1;
    }
  };

  public static final Field<GameObjectCollision, de.mirkosertic.gameengine.core.GameObjectInstance> INSTANCE2 = new Field<GameObjectCollision, de.mirkosertic.gameengine.core.GameObjectInstance>("instance2", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(GameObjectCollision aObject) {
      return aObject.instance2;
    }
  };

  public GameObjectCollisionClassInformation() {
    register(INSTANCE1);
    register(INSTANCE2);
  }
}
