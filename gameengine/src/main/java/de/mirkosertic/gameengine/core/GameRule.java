package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRule {

    private Condition condition;
    private List<Action> actions = new ArrayList<Action>();

    public GameRule(Condition aCondition) {
        condition = aCondition;
    }

    public Condition getCondition() {
        return condition;
    }

    public List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("condition", condition.serialize());
        List<Map<String, Object>> theActionList = new ArrayList<Map<String, Object>>();
        for (Action theAction : actions) {
            theActionList.add(theAction.serialize());
        }
        theResult.put("actions", theActionList);
        return theResult;
    }

    public static GameRule unmarshall(IORegistry aIORegistry, Map<String, Object> aSerializedData) {
        Map<String, Object> theConditionData = (Map<String, Object>) aSerializedData.get("condition");
        String theConditionType = (String) theConditionData.get(Condition.TYPE_ATTRIBUTE);
        Condition theCondition = aIORegistry.getConditionUnmarshallerFor(theConditionType).unmarshall(theConditionData);
        GameRule theResult =  new GameRule(theCondition);
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