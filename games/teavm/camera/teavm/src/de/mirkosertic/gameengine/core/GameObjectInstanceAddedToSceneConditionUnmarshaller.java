package de.mirkosertic.gameengine.core;

import java.util.Map;

public class GameObjectInstanceAddedToSceneConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return GameObjectInstanceAddedToSceneCondition.TYPE_VALUE;
    }

    @Override
    public GameObjectInstanceAddedToSceneCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return GameObjectInstanceAddedToSceneCondition.unmarshall(aGameScene, aSerializedData);
    }
}
