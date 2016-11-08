package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameObjectRemovedFromSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<GameObjectRemovedFromScene, de.mirkosertic.gameengine.core.GameScene> SCENE = new Field<GameObjectRemovedFromScene, de.mirkosertic.gameengine.core.GameScene>("scene", de.mirkosertic.gameengine.core.GameScene.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameScene getValue(GameObjectRemovedFromScene aObject) {
      return aObject.scene;
    }
  };

  public final Field<GameObjectRemovedFromScene, de.mirkosertic.gameengine.core.GameObject> OBJECT = new Field<GameObjectRemovedFromScene, de.mirkosertic.gameengine.core.GameObject>("object", de.mirkosertic.gameengine.core.GameObject.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObject getValue(GameObjectRemovedFromScene aObject) {
      return aObject.object;
    }
  };

  public GameObjectRemovedFromSceneClassInformation() {
    register(SCENE);
    register(OBJECT);
  }
}
