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

import java.util.HashMap;
import java.util.Map;

public class IORegistry {

    private final Map<BehaviorType, BehaviorTemplateUnmarshaller> registeredBehaviorTemplateUnmarshaller;
    private final Map<BehaviorType, BehaviorUnmarshaller> registeredBehaviorUnmarshaller;
    private final Map<String, ConditionUnmarshaller> registeredConditionUnmarshaller;
    private final Map<String, ActionUnmarshaller> registeredActionUnmarshaller;
    private final Map<String, GameSceneEffectUnmarshaller> sceneEffectUnmarsheller;

    IORegistry() {
        registeredBehaviorTemplateUnmarshaller = new HashMap<>();
        registeredBehaviorUnmarshaller = new HashMap<>();
        registeredConditionUnmarshaller = new HashMap<>();
        registeredActionUnmarshaller = new HashMap<>();
        sceneEffectUnmarsheller = new HashMap<>();
    }

    public void registerBehaviorTemplateUnmarshaller(BehaviorTemplateUnmarshaller aUnmarshaller) {
        registeredBehaviorTemplateUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerBehaviorUnmarshaller(BehaviorUnmarshaller aUnmarshaller) {
        registeredBehaviorUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerConditionUnmarshaller(ConditionUnmarshaller aUnmarshaller) {
        registeredConditionUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerActionUnmarshaller(ActionUnmarshaller aUnmarshaller) {
        registeredActionUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerGameSceneEffectUnmarshaller(GameSceneEffectUnmarshaller aUnmarshaller) {
        sceneEffectUnmarsheller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public BehaviorTemplateUnmarshaller getBehaviorTemplateUnmarshallerFor(BehaviorType aTypeKey) {
        return registeredBehaviorTemplateUnmarshaller.get(aTypeKey);
    }

    public BehaviorUnmarshaller getBehaviorUnmarshallerFor(BehaviorType aTypeKey) {
        return registeredBehaviorUnmarshaller.get(aTypeKey);
    }

    public ConditionUnmarshaller getConditionUnmarshallerFor(String aTypeKey) {
        return registeredConditionUnmarshaller.get(aTypeKey);
    }

    public ActionUnmarshaller getActionUnmarshallerFor(String aTypeKey) {
        return registeredActionUnmarshaller.get(aTypeKey);
    }

    public GameSceneEffectUnmarshaller getSceneEffectUnmarshallerFor(String aTypeKey) {
        return sceneEffectUnmarsheller.get(aTypeKey);
    }
}