package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface BehaviorUnmarshaller<T extends Behavior> {

    String getTypeKey();

    T deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData);
}
