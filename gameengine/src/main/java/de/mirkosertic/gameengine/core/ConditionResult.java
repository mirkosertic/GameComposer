package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConditionResult {

    public static final ConditionResult NOT_FULFILLED = new ConditionResult(false, new ArrayList<GameObjectInstance>());
    public static final ConditionResult FULFILLED = new ConditionResult(true, new ArrayList<GameObjectInstance>());

    private final boolean conditionTrue;
    private final List<GameObjectInstance> affectedInstances;

    public ConditionResult(boolean aConditionTrue, List<GameObjectInstance> aAffectedInstances) {
        conditionTrue = aConditionTrue;
        affectedInstances = aAffectedInstances;
    }

    public boolean isConditionTrue() {
        return conditionTrue;
    }

    public List<GameObjectInstance> getAffectedInstances() {
        return Collections.unmodifiableList(affectedInstances);
    }
}
