package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventSheet {

    private Property<String> nameProperty;
    private List<GameRule> rules;
    private GameScene gameScene;

    public EventSheet(GameScene aGameScene) {
        GameEventManager theEventManager = aGameScene.getRuntime().getEventManager();

        nameProperty = new Property<String>(this, "name", theEventManager);
        rules = new ArrayList<GameRule>();
        gameScene = aGameScene;
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public Property<String> nameProperty() {
        return nameProperty;
    }

    public List<GameRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        List<Map<String, Object>> theRuleList = new ArrayList<Map<String, Object>>();
        for (GameRule theRule : rules) {
            theRuleList.add(theRule.serialize());
        }
        theResult.put("rules", theRuleList);
        return theResult;
    }

    public static EventSheet unmarshall(IORegistry aIORegistry, GameScene aGameScene, Map<String, Object> aSerializedData) {
        EventSheet theResult = new EventSheet(aGameScene);
        List<Map<String, Object>> theRules = (List<Map<String, Object>>) aSerializedData.get("rules");
        if (theRules != null) {
            for (Map<String, Object> theRuleData : theRules) {
                theResult.rules.add(GameRule.unmarshall(aIORegistry, theRuleData));
            }
        }
        return theResult;
    }

    public void addRule(GameRule aNewRule) {
        rules.add(aNewRule);
    }

    public void removeRule(GameRule aGameRule) {
        rules.remove(aGameRule);
    }
}
