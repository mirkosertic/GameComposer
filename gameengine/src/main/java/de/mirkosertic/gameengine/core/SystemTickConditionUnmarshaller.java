package de.mirkosertic.gameengine.core;

import java.util.Map;

public class SystemTickConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return SystemTickCondition.TYPE_VALUE;
    }

    @Override
    public Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return SystemTickCondition.unmarshall(aGameScene, aSerializedData);
    }
}
