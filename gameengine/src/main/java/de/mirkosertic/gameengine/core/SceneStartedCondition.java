package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

public class SceneStartedCondition  implements Condition {

    static final String TYPE_VALUE = "SceneStartedCondition";

    @UsedByReflection
    public SceneStartedCondition() {
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof SceneStarted) {
            SceneStarted theEvent = (SceneStarted) aEvent;
            return new ConditionResult(true, theEvent, aScene.getInstances());
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        return theResult;
    }

    public static SceneStartedCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        SceneStartedCondition theResult = new SceneStartedCondition();
        return theResult;
    }
}