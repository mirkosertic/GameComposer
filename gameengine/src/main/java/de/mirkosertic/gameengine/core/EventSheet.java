package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

import java.util.*;

public class EventSheet {

    public static final String NAME_PROPERTY = "name";

    private final Property<String> nameProperty;
    private GameRule[] rules;

    private final GameScene gameScene;

    EventSheet(GameScene aGameScene) {
        GameEventManager theEventManager = aGameScene.getRuntime().getEventManager();

        nameProperty = new Property<String>(String.class, this, NAME_PROPERTY, theEventManager);
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
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(NAME_PROPERTY, nameProperty.get());
        List<Map<String, Object>> theRuleList = new ArrayList<Map<String, Object>>();
        for (GameRule theRule : rules) {
            theRuleList.add(theRule.serialize());
        }
        theResult.put("rules", theRuleList);
        return theResult;
    }

    public static EventSheet unmarshall(IORegistry aIORegistry, GameScene aGameScene, Map<String, Object> aSerializedData) {
        EventSheet theResult = new EventSheet(aGameScene);

        String theName = (String) aSerializedData.get(NAME_PROPERTY);
        if (theName != null) {
            theResult.nameProperty.setQuietly(theName);
        }

        List<Map<String, Object>> theRules = (List<Map<String, Object>>) aSerializedData.get("rules");
        List<GameRule> theRuleList = new ArrayList<GameRule>();
        if (theRules != null) {
            for (Map<String, Object> theRuleData : theRules) {
                theRuleList.add(GameRule.unmarshall(aIORegistry, theResult, theRuleData));
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
