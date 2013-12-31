package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class SystemTickCondition implements Condition {

    static final String TYPE_VALUE = "SystemTickCondition";

    private final Property<Long> everyTicks;

    @UsedByReflection
    public SystemTickCondition() {
        everyTicks = new Property<Long>(this, "everyTicks", 1l);
    }

    public Property<Long> everyTicksProperty() {
        return everyTicks;
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof SystemTick) {
            SystemTick theTick = (SystemTick) aEvent;
            if (theTick.totalTicks % everyTicks.get() == 0) {
                return new ConditionResult(true, aScene.getInstances());
            }
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("everyTicks", Long.toString(everyTicks.get()));
        return theResult;
    }

    public static Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        SystemTickCondition theResult = new SystemTickCondition();
        theResult.everyTicks.setQuietly(Long.valueOf((String) aSerializedData.get("everyTicks")));
        return theResult;
    }
}
