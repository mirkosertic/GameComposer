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

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventSheet {

    public static final String NAME_PROPERTY = "name";

    private final Property<String> nameProperty;
    private GameRule[] rules;

    private final GameScene gameScene;

    EventSheet(GameScene aGameScene) {
        GameEventManager theEventManager = aGameScene.getRuntime().getEventManager();

        nameProperty = new Property<>(String.class, this, NAME_PROPERTY, theEventManager);
        rules = new GameRule[]{};
        gameScene = aGameScene;
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public Property<String> nameProperty() {
        return nameProperty;
    }

    public GameRule[] getRules() {
        return rules;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(NAME_PROPERTY, nameProperty.get());
        List<Map<String, Object>> theRuleList = new ArrayList<>();
        for (GameRule theRule : rules) {
            theRuleList.add(theRule.serialize());
        }
        theResult.put("rules", theRuleList);
        return theResult;
    }

    public static EventSheet unmarshall(IORegistry aIORegistry, GameScene aGameScene, Map<String, Object> aSerializedData) {
        EventSheet theResult = new EventSheet(aGameScene);

        String theName = (String) aSerializedData.get(NAME_PROPERTY);
        theResult.nameProperty.setQuietly(theName);

        List<Map<String, Object>> theRules = (List<Map<String, Object>>) aSerializedData.get("rules");
        List<GameRule> theRuleList = new ArrayList<>();
        if (theRules != null) {
            for (Map<String, Object> theRuleData : theRules) {
                theRuleList.add(GameRule.deserialize(aIORegistry, theResult, theRuleData));
            }
        }
        theResult.rules = theRuleList.toArray(new GameRule[theRuleList.size()]);
        return theResult;
    }

    public void addRule(GameRule aNewRule) {
        List<GameRule> theRules = ArrayUtils.asList(rules);
        theRules.add(aNewRule);
        rules = theRules.toArray(new GameRule[theRules.size()]);
    }

    public void removeRule(GameRule aGameRule) {
        List<GameRule> theRules = ArrayUtils.asList(rules);
        theRules.remove(aGameRule);
        rules = theRules.toArray(new GameRule[theRules.size()]);
    }
}
