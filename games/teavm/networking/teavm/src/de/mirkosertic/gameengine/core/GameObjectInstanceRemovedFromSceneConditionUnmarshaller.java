package de.mirkosertic.gameengine.core;

import java.util.Map;

public class GameObjectInstanceRemovedFromSceneConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return GameObjectInstanceRemovedFromSceneCondition.TYPE_VALUE;
    }

    @Override
    public GameObjectInstanceRemovedFromSceneCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return GameObjectInstanceRemovedFromSceneCondition.unmarshall(aGameScene, aSerializedData);
    }
}
