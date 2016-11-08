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
package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.HashMap;
import java.util.Map;

public class KillProcessesForInstanceAction implements Action {

    public static final String TYPE_VALUE = "KillProcessesForInstanceAction";

    @UsedByReflection
    public KillProcessesForInstanceAction() {
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        GameEventManager theEventManager = aScene.getRuntime().getEventManager();
        for (GameObjectInstance theInstance : aResult.getAffectedInstances()) {
            theEventManager.fire(new KillProcessesForInstance(theInstance));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        return theResult;
    }

    public static KillProcessesForInstanceAction unmarshall() {
        return new KillProcessesForInstanceAction();
    }
}
