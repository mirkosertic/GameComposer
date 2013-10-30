package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface GameComponentUnmarshaller<T extends GameComponent> {

    String getTypeKey();

    T deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData);
}
