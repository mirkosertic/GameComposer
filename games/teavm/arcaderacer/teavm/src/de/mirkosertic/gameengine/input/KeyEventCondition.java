/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.GameKeyCode;

import java.util.HashMap;
import java.util.Map;

public class KeyEventCondition implements Condition {

    public static enum KeyEventType {
        PRESSED, RELEASED
    }

    public static final String KEY_CODE_PROPERTY = "keyCode";
    public static final String EVENT_TYPE_PROPERTY = "eventType";

    static final String TYPE_VALUE = "KeyEventCondition";

    private final Property<GameKeyCode> keyCode;
    private final Property<KeyEventType> eventType;

    @UsedByReflection
    public KeyEventCondition() {
        keyCode = new Property<>(GameKeyCode.class, this, KEY_CODE_PROPERTY, (GameKeyCode) null);
        eventType = new Property<>(KeyEventType.class, this, EVENT_TYPE_PROPERTY, KeyEventType.PRESSED);
    }

    public Property<GameKeyCode> keyCodeProperty() {
        return keyCode;
    }

    public Property<KeyEventType> eventTypeProperty() {
        return eventType;
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof KeyPressed) {
            switch (eventType.get()) {
                case PRESSED:
                    GameKeyCode theKeyCode = keyCode.get();
                    KeyPressed theKeyPressed = (KeyPressed) aEvent;
                    if (theKeyPressed.keyCode == theKeyCode || theKeyCode == GameKeyCode.ANY) {
                        return new ConditionResult(true, aEvent, aScene.getInstances());
                    }
                    break;
                case RELEASED:
                    // Is not handled
                    break;
            }
            return ConditionResult.NOT_FULFILLED;
        }
        if (aEvent instanceof KeyReleased) {
            switch (eventType.get()) {
                case PRESSED:
                    // Is not handled
                    break;
                case RELEASED:
                    GameKeyCode theKeyCode = keyCode.get();
                    KeyReleased theKeyReleased = (KeyReleased) aEvent;
                    if (theKeyReleased.keyCode == theKeyCode || theKeyCode == GameKeyCode.ANY) {
                        return new ConditionResult(true, aEvent, aScene.getInstances());
                    }
                    break;
            }
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put(KEY_CODE_PROPERTY, keyCode.get().name());
        theResult.put(EVENT_TYPE_PROPERTY, eventType.get().name());
        return theResult;
    }

    public static KeyEventCondition unmarshall(Map<String, Object> aSerializedData) {
        KeyEventCondition theResult = new KeyEventCondition();
        theResult.keyCode.setQuietly(GameKeyCode.valueOf((String) aSerializedData.get(KEY_CODE_PROPERTY)));
        theResult.eventType.setQuietly(KeyEventType.valueOf((String) aSerializedData.get(EVENT_TYPE_PROPERTY)));
        return theResult;
    }
}
