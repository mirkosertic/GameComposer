package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface ActionUnmarshaller {

    String getTypeKey();

    Action unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene);
}
