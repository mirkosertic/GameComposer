package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRule {

    private Property<String> name;
    private Property<Condition> condition;
    private List<Action> actions = new ArrayList<Action>();

    public GameRule() {
        name = new Property<String>(this, "name", (String) null);
        condition = new Property<Condition>(this, "condition", (Condition) null);
    }

    public Property<String> nameProperty() {
        return name;
    }

    public Property<Condition> conditionProperty() {
        return condition;
    }

    public List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public void addAction(Action aAction) {
        actions.add(aAction);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        if (!name.isNull()) {
            theResult.put("name", name.get());
        }
        if (!condition.isNull()) {
            theResult.put("condition", condition.get().serialize());
        }
        List<Map<String, Object>> theActionList = new ArrayList<Map<String, Object>>();
        for (Action theAction : actions) {
            theActionList.add(theAction.serialize());
        }
        theResult.put("actions", theActionList);
        return theResult;
    }

    public static GameRule unmarshall(IORegistry aIORegistry, EventSheet aEventSheet, Map<String, Object> aSerializedData) {
        GameRule theResult =  new GameRule();

        String theName = (String) aSerializedData.get("name");
        if (theName != null) {
            theResult.nameProperty().setQuietly(theName);
        }

        Map<String, Object> theConditionData = (Map<String, Object>) aSerializedData.get("condition");
        if (theConditionData != null) {
            String theConditionType = (String) theConditionData.get(Condition.TYPE_ATTRIBUTE);
            Condition theCondition = aIORegistry.getConditionUnmarshallerFor(theConditionType).unmarshall(aEventSheet.getGameScene(), theConditionData);
            theResult.condition.setQuietly(theCondition);
        }
        List<Map<String, Object>> theActions = (List<Map<String, Object>>) aSerializedData.get("actions");
        if (theActions != null) {
            for (Map<String, Object> theActionData : theActions) {
                String theActionTypeKey = (String) theActionData.get(Action.TYPE_ATTRIBUTE);
                theResult.actions.add(aIORegistry.getActionUnmarshallerFor(theActionTypeKey).unmarshall(aIORegistry, theActionData));
            }
        }
        return theResult;
    }
}