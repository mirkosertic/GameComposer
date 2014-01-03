package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class KeyEventConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return KeyEventCondition.TYPE_VALUE;
    }

    @Override
    public Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return KeyEventCondition.unmarshall(aSerializedData);
    }
}
