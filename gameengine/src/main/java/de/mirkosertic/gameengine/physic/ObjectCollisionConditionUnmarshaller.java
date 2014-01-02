package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class ObjectCollisionConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return ObjectCollisionCondition.TYPE_VALUE;
    }

    @Override
    public Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return ObjectCollisionCondition.unmarshall(aGameScene, aSerializedData);
    }
}
