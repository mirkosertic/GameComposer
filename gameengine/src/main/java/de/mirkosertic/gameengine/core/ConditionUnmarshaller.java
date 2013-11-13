package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface ConditionUnmarshaller {

    String getTypeKey();

    Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData);
}
