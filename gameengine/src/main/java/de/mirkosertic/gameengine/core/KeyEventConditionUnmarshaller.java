package de.mirkosertic.gameengine.core;

import java.util.Map;

public class KeyEventConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return KeyEventCondition.TYPE_VALUE;
    }

    @Override
    public Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return KeyEventCondition.unmarshall(aGameScene, aSerializedData);
    }
}
