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
package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.type.DistributableUtils;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

@InheritedClassInformation
public class PropertyChanged extends GameEvent implements DistributableEvent {

    public static final GameEventType TYPE = new GameEventType("PropertyChanged");

    private static final PropertyChangedClassInformation CIINSTANCE = new PropertyChangedClassInformation();

    @ReflectiveField
    public final Property property;

    @ReflectiveField
    public final Object oldValue;

    public PropertyChanged(Property aProperty, Object aOldValue) {
        super(TYPE);
        property = aProperty;
        oldValue = aOldValue;
    }

    @ReflectiveMethod
    public Object getOwner() {
        return property.getOwner();
    }

    @Override
    public PropertyChangedClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public boolean isForced() {
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, type.getType());
        theResult.put("eventts", "" + System.currentTimeMillis());
        theResult.put("propertyName", property.getName());
        theResult.put("newValue", DistributableUtils.convert(property.get()));

        Object theOwner = property.getOwner();
        if (theOwner instanceof GameObjectInstance) {
            GameObjectInstance theInstance = (GameObjectInstance) theOwner;
            theResult.put("instanceID", theInstance.uuidProperty().get());
        }
        if (theOwner instanceof Behavior) {
            Behavior theBehavior = (Behavior) theOwner;
            theResult.put("instanceID", theBehavior.getInstance().uuidProperty().get());
            theResult.put("behavior", theBehavior.getType());
        }

        return theResult;
    }

    public static void runEventInScene(Map<String, Object> aEventData, GameScene aGameScene) {
        String thePropertyName = (String ) aEventData.get("propertyName");
        String theInstanceID = (String) aEventData.get("instanceID");
        long theEventTS = Long.parseLong((String) aEventData.get("eventts"));
        if (theInstanceID != null) {
            GameObjectInstance theInstance = aGameScene.findInstanceByID(theInstanceID);
            if (theInstance != null) {
                String theBehaviorType = (String) aEventData.get("behavior");
                if (theBehaviorType == null) {
                    DistributableUtils.setField(theInstance, thePropertyName, aEventData.get("newValue"), theEventTS);
                } else {
                    Object theBehavior = theInstance.findBehaviorByType(theBehaviorType);
                    if (theBehavior != null) {
                        // We KNOW every behavior is Reflectable
                        DistributableUtils.setField((Reflectable) theBehavior, thePropertyName, aEventData.get("newValue"), theEventTS);
                    }
                }
            }
        }
    }
}