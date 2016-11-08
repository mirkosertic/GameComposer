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
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MouseEventCondition implements Condition {

    public static enum FilterType {
        IGNORE_INSTANCES, ALL_INSTANCES, FILTER_BY_OBJECT_TYPE
    }

    public static enum MouseEventType {
        PRESSED, RELEASED
    }

    public static final String EVENT_TYPE_PROPERTY = "eventType";
    public static final String FILTER_TYPE_PROPERTY = "filterType";
    public static final String OBJECT_TYPE_PROPERTY = "objectType";

    static final String TYPE_VALUE = "MouseEventCondition";

    private final Property<MouseEventType> eventType;

    private final Property<FilterType> filterType;

    private final Property<GameObject> objectType;

    @UsedByReflection
    public MouseEventCondition() {
        eventType = new Property<>(MouseEventType.class, this, EVENT_TYPE_PROPERTY, MouseEventType.PRESSED);
        filterType = new Property<>(FilterType.class, this, FILTER_TYPE_PROPERTY, FilterType.FILTER_BY_OBJECT_TYPE);
        objectType = new Property<>(GameObject.class, this, OBJECT_TYPE_PROPERTY, (GameObject) null);
    }

    public Property<MouseEventType> eventTypeProperty() {
        return eventType;
    }

    public Property<FilterType> filterTypeProperty() {
        return filterType;
    }

    public Property<GameObject> objectTypeProperty() {
        return objectType;
    }

    private ConditionResult createFrom(GameEvent aEvent, GameObjectInstance[] aInstances) {
        switch (filterType.get()) {
            case ALL_INSTANCES:
                return new ConditionResult(true, aEvent, aInstances);
            case FILTER_BY_OBJECT_TYPE:
                GameObject theResult = objectType.get();
                List<GameObjectInstance> theInstances = new ArrayList<>();
                if (theResult != null) {
                    for (GameObjectInstance theInstance : aInstances) {
                        if (theInstance.getOwnerGameObject() == theResult) {
                            theInstances.add(theInstance);
                        }
                    }
                }
                GameObjectInstance[] theFiltered = theInstances.toArray(new GameObjectInstance[theInstances.size()]);
                return new ConditionResult(true, aEvent, theFiltered);
            case IGNORE_INSTANCES:
                return new ConditionResult(true, aEvent, new GameObjectInstance[0]);
        }
        throw new IllegalStateException("Not handled filter type : " + filterType.get());
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof MousePressed) {
            MousePressed theMouseEvent = (MousePressed) aEvent;
            switch (eventType.get()) {
                case PRESSED:
                    return createFrom(aEvent, theMouseEvent.affectedInstances);
                case RELEASED:
                    // Is not handled
                    break;
            }
            return ConditionResult.NOT_FULFILLED;
        }
        if (aEvent instanceof MouseReleased) {
            MouseReleased theMouseEvent = (MouseReleased) aEvent;
            switch (eventType.get()) {
            case PRESSED:
                // Is not handled
                break;
            case RELEASED:
                // Is not handled
                return createFrom(aEvent, theMouseEvent.affectedInstances);
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
        theResult.put(FILTER_TYPE_PROPERTY, filterType.get().name());
        if (!objectType.isNull()) {
            theResult.put(OBJECT_TYPE_PROPERTY, objectType.get().uuidProperty().get());
        }
        return theResult;
    }

    public static MouseEventCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        MouseEventCondition theResult = new MouseEventCondition();
        theResult.eventType.setQuietly(MouseEventType.valueOf((String) aSerializedData.get(EVENT_TYPE_PROPERTY)));
        theResult.filterType.setQuietly(FilterType.valueOf((String) aSerializedData.get(FILTER_TYPE_PROPERTY)));
        String theObjectID = (String) aSerializedData.get(OBJECT_TYPE_PROPERTY);
        if (theObjectID != null) {
            theResult.objectType.setQuietly(aGameScene.findObjectByID(theObjectID));
        }
        return theResult;
    }
}