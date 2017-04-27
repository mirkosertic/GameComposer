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
package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class StaticBehavior implements Behavior, Static, Reflectable<StaticClassInformation> {

    private static final StaticClassInformation CIINSTANCE = new StaticClassInformation();
    
    public static final BehaviorType TYPE = new BehaviorType("Static");

    private final GameObjectInstance objectInstance;

    StaticBehavior(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    @Override
    public StaticClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public StaticBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(StaticBehaviorTemplate.TYPE);
    }

    @Override
    public void delete() {
        objectInstance.getOwnerGameObject().getGameScene().removeBehaviorFrom(objectInstance.getOwnerGameObject(), this);
    }

    @Override
    public GameObjectInstance getInstance() {
        return objectInstance;
    }

    @Override
    public void markAsRemoteObject() {
    }

    public static StaticBehavior deserialize(GameObjectInstance aObjectInstance) {
        return new StaticBehavior(aObjectInstance);
    }
}
