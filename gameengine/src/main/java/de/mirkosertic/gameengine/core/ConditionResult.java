package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class ConditionResult {

    public static final ConditionResult NOT_FULFILLED = new ConditionResult(false, null, new GameObjectInstance[0]);

    private final boolean conditionTrue;
    private final GameEvent event;
    private final GameObjectInstance[] affectedInstances;

    public ConditionResult(boolean aConditionTrue, GameEvent aEvent, GameObjectInstance[] aAffectedInstances) {
        conditionTrue = aConditionTrue;
        affectedInstances = aAffectedInstances;
        event = aEvent;
    }

    public boolean isConditionTrue() {
        return conditionTrue;
    }

    public GameObjectInstance[] getAffectedInstances() {
        return affectedInstances;
    }

    public GameEvent getEvent() {
        return event;
    }
}
