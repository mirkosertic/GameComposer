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
package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class ConditionResult {

    public static final ConditionResult NOT_FULFILLED = new ConditionResult(false, null, new GameObjectInstance[0]);

    private final boolean conditionTrue;
    private final GameEvent event;
    private final GameObjectInstance[] affectedInstances;

    public ConditionResult(boolean aConditionTrue, GameEvent aEvent, GameObjectInstance[] aAffectedInstances) {
        conditionTrue = aConditionTrue;
        affectedInstances = aAffectedInstances;
        event = aEvent;
    }

    public boolean isConditionTrue() {
        return conditionTrue;
    }

    public GameObjectInstance[] getAffectedInstances() {
        return affectedInstances;
    }

    public GameEvent getEvent() {
        return event;
    }
}
