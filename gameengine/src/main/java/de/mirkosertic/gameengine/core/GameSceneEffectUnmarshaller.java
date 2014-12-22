package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface GameSceneEffectUnmarshaller {

    String getTypeKey();

    GameSceneEffect unmarshall(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> aObjectData);
}