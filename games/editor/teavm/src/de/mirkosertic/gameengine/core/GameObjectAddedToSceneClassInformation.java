package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameObjectAddedToSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<GameObjectAddedToScene, de.mirkosertic.gameengine.core.GameScene> SCENE = new Field<GameObjectAddedToScene, de.mirkosertic.gameengine.core.GameScene>("scene", de.mirkosertic.gameengine.core.GameScene.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameScene getValue(GameObjectAddedToScene aObject) {
      return aObject.scene;
    }
  };

  public final Field<GameObjectAddedToScene, de.mirkosertic.gameengine.core.GameObject> OBJECT = new Field<GameObjectAddedToScene, de.mirkosertic.gameengine.core.GameObject>("object", de.mirkosertic.gameengine.core.GameObject.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObject getValue(GameObjectAddedToScene aObject) {
      return aObject.object;
    }
  };

  public GameObjectAddedToSceneClassInformation() {
    register(SCENE);
    register(OBJECT);
  }
}
