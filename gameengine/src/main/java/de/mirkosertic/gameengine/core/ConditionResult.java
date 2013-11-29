package de.mirkosertic.gameengine.core;

public class ConditionResult {

    public static final ConditionResult NOT_FULFILLED = new ConditionResult(false, new GameObjectInstance[0]);

    private final boolean conditionTrue;
    private final GameObjectInstance[] affectedInstances;

    public ConditionResult(boolean aConditionTrue, GameObjectInstance[] aAffectedInstances) {
        conditionTrue = aConditionTrue;
        affectedInstances = aAffectedInstances;
    }

    public boolean isConditionTrue() {
        return conditionTrue;
    }

    public GameObjectInstance[] getAffectedInstances() {
        return affectedInstances;
    }
}
