package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class SystemTickConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return SystemTickCondition.TYPE_VALUE;
    }

    @Override
    public Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return SystemTickCondition.unmarshall(aSerializedData);
    }
}
