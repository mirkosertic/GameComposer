package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.*;

public class GameSceneEffectAddedToSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<GameSceneEffectAddedToScene, de.mirkosertic.gameengine.core.GameScene> SCENE = new Field<GameSceneEffectAddedToScene, de.mirkosertic.gameengine.core.GameScene>("scene", de.mirkosertic.gameengine.core.GameScene.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameScene getValue(GameSceneEffectAddedToScene aObject) {
      return aObject.scene;
    }
  };

  public static final Field<GameSceneEffectAddedToScene, de.mirkosertic.gameengine.core.GameSceneEffect> INSTANCE = new Field<GameSceneEffectAddedToScene, de.mirkosertic.gameengine.core.GameSceneEffect>("instance", de.mirkosertic.gameengine.core.GameSceneEffect.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameSceneEffect getValue(GameSceneEffectAddedToScene aObject) {
      return aObject.instance;
    }
  };

  public GameSceneEffectAddedToSceneClassInformation() {
    register(SCENE);
    register(INSTANCE);
  }
}
