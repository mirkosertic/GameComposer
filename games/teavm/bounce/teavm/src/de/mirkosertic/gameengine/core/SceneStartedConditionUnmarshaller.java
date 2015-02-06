package de.mirkosertic.gameengine.core;

import java.util.Map;

public class SceneStartedConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return SceneStartedCondition.TYPE_VALUE;
    }

    @Override
    public SceneStartedCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return SceneStartedCondition.unmarshall(aGameScene, aSerializedData);
    }
}