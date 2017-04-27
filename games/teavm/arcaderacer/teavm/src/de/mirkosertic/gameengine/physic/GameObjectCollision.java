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

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventType;

@InheritedClassInformation
public class GameObjectCollision extends GameEvent {

    public static final GameEventType TYPE = new GameEventType("GameObjectCollision");

    private static final GameObjectCollisionClassInformation CIINSTANCE = new GameObjectCollisionClassInformation();

    @ReflectiveField
    public final GameObjectInstance instance1;

    @ReflectiveField
    public final GameObjectInstance instance2;

    public GameObjectCollision(GameObjectInstance aInstance1, GameObjectInstance aInstance2) {
        super(TYPE);
        instance1 = aInstance1;
        instance2 = aInstance2;
    }

    public GameObjectInstance getOtherInstanceOrNullIfNotAffected(GameObjectInstance aOtherInstance) {
        if (instance1 == aOtherInstance) {
            return instance2;
        }
        if (instance2 == aOtherInstance) {
            return instance1;
        }
        return null;
    }

    @Override
    public GameObjectCollisionClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
