package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class MouseEventConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return MouseEventCondition.TYPE_VALUE;
    }

    @Override
    public Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return MouseEventCondition.unmarshall(aGameScene, aSerializedData);
    }
}
