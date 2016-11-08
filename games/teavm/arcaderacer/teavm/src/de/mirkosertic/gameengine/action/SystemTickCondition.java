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
package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class SystemTickCondition implements Condition {

    static final String TYPE_VALUE = "SystemTickCondition";

    public static final String EVERY_TICKS_PROPERTY = "everyTicks";

    private final Property<Long> everyTicks;

    @UsedByReflection
    public SystemTickCondition() {
        everyTicks = new Property<>(Long.class, this, EVERY_TICKS_PROPERTY, 1l);
    }

    public Property<Long> everyTicksProperty() {
        return everyTicks;
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof SystemTick) {
            SystemTick theTick = (SystemTick) aEvent;
            if (theTick.totalTicks % everyTicks.get() == 0) {
                return new ConditionResult(true, aEvent, aScene.getInstances());
            }
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put(EVERY_TICKS_PROPERTY, Long.toString(everyTicks.get()));
        return theResult;
    }

    public static Condition unmarshall(Map<String, Object> aSerializedData) {
        SystemTickCondition theResult = new SystemTickCondition();
        theResult.everyTicks.setQuietly(Long.valueOf((String) aSerializedData.get(EVERY_TICKS_PROPERTY)));
        return theResult;
    }
}
