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

import java.util.HashMap;
import java.util.Map;

public class SceneStartedCondition  implements Condition {

    static final String TYPE_VALUE = "SceneStartedCondition";

    @UsedByReflection
    public SceneStartedCondition() {
    }

    @Override
    public ConditionResult appliesTo(GameScene aScene, GameEvent aEvent) {
        if (aEvent instanceof SceneStarted) {
            SceneStarted theEvent = (SceneStarted) aEvent;
            return new ConditionResult(true, theEvent, aScene.getInstances());
        }
        return ConditionResult.NOT_FULFILLED;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        return theResult;
    }

    public static SceneStartedCondition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        SceneStartedCondition theResult = new SceneStartedCondition();
        return theResult;
    }
}