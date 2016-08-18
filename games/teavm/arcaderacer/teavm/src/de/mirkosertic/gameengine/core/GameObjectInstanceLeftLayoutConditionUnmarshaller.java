package de.mirkosertic.gameengine.core;

import java.util.Map;

public class GameObjectInstanceLeftLayoutConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return GameObjectInstanceLeftLayoutCondition.TYPE_VALUE;
    }

    @Override
    public GameObjectInstanceLeftLayoutCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return GameObjectInstanceLeftLayoutCondition.unmarshall(aGameScene, aSerializedData);
    }
}
