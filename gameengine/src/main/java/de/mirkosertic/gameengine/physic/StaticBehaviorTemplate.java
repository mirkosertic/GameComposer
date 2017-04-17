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

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class StaticBehaviorTemplate implements BehaviorTemplate<StaticBehavior>, Static, Reflectable<StaticClassInformation> {

    public static final BehaviorType TYPE = new BehaviorType("StaticBehaviorTemplate");

    private static final StaticClassInformation CIINSTANCE = new StaticClassInformation();

    private final GameObject owner;

    @UsedByReflection
    public StaticBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        owner = aOwner;
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
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public StaticBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new StaticBehavior(aInstance);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(StaticBehavior.TYPE_ATTRIBUTE, StaticBehavior.TYPE);
        return theResult;
    }

    @Override
    public void delete() {
        owner.getGameScene().removeBehaviorFrom(owner, this);
    }

    public static StaticBehaviorTemplate deserialize(GameEventManager aEventManager, GameObject aOwner) {
        return new StaticBehaviorTemplate(aEventManager, aOwner);
    }
}