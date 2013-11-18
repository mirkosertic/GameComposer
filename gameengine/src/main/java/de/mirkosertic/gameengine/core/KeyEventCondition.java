package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

public class KeyEventCondition implements Condition {

    public static enum KeyEventType {
        PRESSED, RELEASED
    }

    public static final String TYPE_VALUE = "KeyEventCondition";

    private final Property<GameKeyCode> keyCode;
    private final Property<KeyEventType> eventType;

    public KeyEventCondition() {
        keyCode = new Property<GameKeyCode>(this, "keyCode", (GameKeyCode) null);
        eventType = new Property<KeyEventType>(this, "eventType", KeyEventType.PRESSED);
    }

    public Property<GameKeyCode> keyCodeProperty() {
        return keyCode;
    }

    public Property<KeyEventType> eventTypeProperty() {
        return eventType;
    }

    @Override
    public boolean appliesTo(GameEvent aEvent) {
        switch (eventType.get()) {
            case PRESSED:
                if (aEvent instanceof KeyPressed) {
                    KeyPressed theKeyPressed = (KeyPressed) aEvent;
                    return theKeyPressed.keyCodeProperty().get() == keyCode.get();
                }
                return false;
            case RELEASED:
                if (aEvent instanceof KeyReleased) {
                    KeyReleased theKeyReleased = (KeyReleased) aEvent;
                    return theKeyReleased.keyCodeProperty().get() == keyCode.get();
                }
                return false;

        }
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("eventtype", eventType.get());
        theResult.put("keyCode", keyCode.get().name());
        theResult.put("eventType", eventType.get().name());
        return theResult;
    }

    public static KeyEventCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        KeyEventCondition theResult = new KeyEventCondition();
        theResult.keyCode.setQuietly(GameKeyCode.valueOf((String) aSerializedData.get("keyCode")));
        theResult.eventType.setQuietly(KeyEventType.valueOf((String) aSerializedData.get("eventType")));
        return theResult;
    }
}