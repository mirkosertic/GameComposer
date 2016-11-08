package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameSceneEffectRemovedFromSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<GameSceneEffectRemovedFromScene, de.mirkosertic.gameengine.core.GameScene> SCENE = new Field<GameSceneEffectRemovedFromScene, de.mirkosertic.gameengine.core.GameScene>("scene", de.mirkosertic.gameengine.core.GameScene.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameScene getValue(GameSceneEffectRemovedFromScene aObject) {
      return aObject.scene;
    }
  };

  public final Field<GameSceneEffectRemovedFromScene, de.mirkosertic.gameengine.core.GameSceneEffect> INSTANCE = new Field<GameSceneEffectRemovedFromScene, de.mirkosertic.gameengine.core.GameSceneEffect>("instance", de.mirkosertic.gameengine.core.GameSceneEffect.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameSceneEffect getValue(GameSceneEffectRemovedFromScene aObject) {
      return aObject.instance;
    }
  };

  public GameSceneEffectRemovedFromSceneClassInformation() {
    register(SCENE);
    register(INSTANCE);
  }
}
