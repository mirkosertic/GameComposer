package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.*;

public class GameObjectAddedToSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<GameObjectAddedToScene, de.mirkosertic.gameengine.core.GameScene> SCENE = new Field<GameObjectAddedToScene, de.mirkosertic.gameengine.core.GameScene>("scene", de.mirkosertic.gameengine.core.GameScene.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameScene getValue(GameObjectAddedToScene aObject) {
      return aObject.scene;
    }
  };

  public static final Field<GameObjectAddedToScene, de.mirkosertic.gameengine.core.GameObject> OBJECT = new Field<GameObjectAddedToScene, de.mirkosertic.gameengine.core.GameObject>("object", de.mirkosertic.gameengine.core.GameObject.class) {
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
