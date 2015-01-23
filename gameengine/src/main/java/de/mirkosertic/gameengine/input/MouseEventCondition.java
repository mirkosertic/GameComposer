package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class MouseEventCondition implements Condition {

    public static enum MouseEventType {
        PRESSED, RELEASED
    }

    public static final String EVENT_TYPE_PROPERTY = "eventType";

    static final String TYPE_VALUE = "MouseEventCondition";

    private final Property<MouseEventType> eventType;

    @UsedByReflection
    public MouseEventCondition() {
        eventType = new Property<>(MouseEventType.class, this, EVENT_TYPE_PROPERTY, MouseEventType.PRESSED);
    }

    public Property<MouseEventType> eventTypeProperty() {
        return eventType;
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof MousePressed) {
            switch (eventType.get()) {
                case PRESSED:
                    //TODO: Try to find
                    return new ConditionResult(true, aEvent, aScene.getInstances());
                case RELEASED:
                    // Is not handled
                    break;
            }
            return ConditionResult.NOT_FULFILLED;
        }
        if (aEvent instanceof MouseReleased) {
            switch (eventType.get()) {
            case PRESSED:
                // Is not handled
                break;
            case RELEASED:
                // Is not handled
                //TODO: Try to find
                return new ConditionResult(true, aEvent, aScene.getInstances());
            }
            return ConditionResult.NOT_FULFILLED;
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put(EVENT_TYPE_PROPERTY, eventType.get().name());
        return theResult;
    }

    public static MouseEventCondition unmarshall(Map<String, Object> aSerializedData) {
        MouseEventCondition theResult = new MouseEventCondition();
        theResult.eventType.setQuietly(MouseEventType.valueOf((String) aSerializedData.get(EVENT_TYPE_PROPERTY)));
        return theResult;
    }
}
