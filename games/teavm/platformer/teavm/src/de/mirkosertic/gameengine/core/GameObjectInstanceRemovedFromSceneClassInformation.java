package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameObjectInstanceRemovedFromSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<GameObjectInstanceRemovedFromScene, de.mirkosertic.gameengine.core.GameScene> SCENE = new Field<GameObjectInstanceRemovedFromScene, de.mirkosertic.gameengine.core.GameScene>("scene", de.mirkosertic.gameengine.core.GameScene.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameScene getValue(GameObjectInstanceRemovedFromScene aObject) {
      return aObject.scene;
    }
  };

  public final Field<GameObjectInstanceRemovedFromScene, de.mirkosertic.gameengine.core.GameObjectInstance> INSTANCE = new Field<GameObjectInstanceRemovedFromScene, de.mirkosertic.gameengine.core.GameObjectInstance>("instance", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
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
