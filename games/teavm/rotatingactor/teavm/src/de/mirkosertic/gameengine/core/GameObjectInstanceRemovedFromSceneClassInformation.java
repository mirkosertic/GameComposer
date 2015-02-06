package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.*;

public class GameObjectInstanceRemovedFromSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<GameObjectInstanceRemovedFromScene, de.mirkosertic.gameengine.core.GameScene> SCENE = new Field<GameObjectInstanceRemovedFromScene, de.mirkosertic.gameengine.core.GameScene>("scene", de.mirkosertic.gameengine.core.GameScene.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameScene getValue(GameObjectInstanceRemovedFromScene aObject) {
      return aObject.scene;
    }
  };

  public static final Field<GameObjectInstanceRemovedFromScene, de.mirkosertic.gameengine.core.GameObjectInstance> INSTANCE = new Field<GameObjectInstanceRemovedFromScene, de.mirkosertic.gameengine.core.GameObjectInstance>("instance", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(GameObjectInstanceRemovedFromScene aObject) {
      return aObject.instance;
    }
  };

  public GameObjectInstanceRemovedFromSceneClassInformation() {
    register(SCENE);
    register(INSTANCE);
  }
}
