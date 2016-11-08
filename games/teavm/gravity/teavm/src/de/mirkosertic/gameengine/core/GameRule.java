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
import de.mirkosertic.gameengine.event.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRule {

    public static final String NAME_PROPERTY = "name";
    public static final String CONDITION_PROPERTY = "condition";

    private final Property<String> name;
    private final Property<Condition> condition;
    private Action[] actions;

    public GameRule() {
        actions = new Action[0];
        name = new Property<>(String.class, this, NAME_PROPERTY, (String) null);
        condition = new Property<>(Condition.class, this, CONDITION_PROPERTY, (Condition) null);
    }

    public Property<String> nameProperty() {
        return name;
    }

    public Property<Condition> conditionProperty() {
        return condition;
    }

    public Action[] getActions() {
        return actions;
    }

    public void addAction(Action aAction) {
        List<Action> theActions = ArrayUtils.asList(actions);
        theActions.add(aAction);
        actions = theActions.toArray(new Action[theActions.size()]);
    }

    public void removeAction(Action aAction) {
        List<Action> theActions = ArrayUtils.asList(actions);
        theActions.remove(aAction);
        actions = theActions.toArray(new Action[theActions.size()]);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        if (!name.isNull()) {
            theResult.put(NAME_PROPERTY, name.get());
        }
        if (!condition.isNull()) {
            theResult.put(CONDITION_PROPERTY, condition.get().serialize());
        }
        List<Map<String, Object>> theActionList = new ArrayList<>();
        for (Action theAction : actions) {
            theActionList.add(theAction.serialize());
        }
        theResult.put("actions", theActionList);
        return theResult;
    }

    public static GameRule deserialize(IORegistry aIORegistry, EventSheet aEventSheet, Map<String, Object> aSerializedData) {
        GameRule theResult =  new GameRule();

        String theName = (String) aSerializedData.get(NAME_PROPERTY);
        if (theName != null) {
            theResult.nameProperty().setQuietly(theName);
        }

        Map<String, Object> theConditionData = (Map<String, Object>) aSerializedData.get(CONDITION_PROPERTY);
        if (theConditionData != null) {
            String theConditionType = (String) theConditionData.get(Condition.TYPE_ATTRIBUTE);
            Condition theCondition = aIORegistry.getConditionUnmarshallerFor(theConditionType).unmarshall(aEventSheet.getGameScene(), theConditionData);
            theResult.condition.setQuietly(theCondition);
        }
        List<Map<String, Object>> theActions = (List<Map<String, Object>>) aSerializedData.get("actions");
        List<Action> theActionList = new ArrayList<>();
        if (theActions != null) {
            for (Map<String, Object> theActionData : theActions) {
                String theActionTypeKey = (String) theActionData.get(Action.TYPE_ATTRIBUTE);
                theActionList.add(aIORegistry.getActionUnmarshallerFor(theActionTypeKey).unmarshall(theActionData, aEventSheet.getGameScene()));
            }
        }
        theResult.actions = theActionList.toArray(new Action[theActionList.size()]);
        return theResult;
    }
}