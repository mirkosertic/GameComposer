package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class RunSceneAction implements Action {

    public static final String TYPE_VALUE = "RunSceneAction";

    private final Property<String> gameScene;

    public RunSceneAction() {
        gameScene = new Property<String>(this, "gameScene", (String) null);
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        GameEventManager theManager = aScene.getRuntime().getEventManager();
        theManager.fire(new RunScene(gameScene.get()));
    }

    public Property<String> gameSceneProperty() {
        return gameScene;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        if (gameScene.get() != null) {
            theResult.put("gameScene", gameScene.get());
        }
        return theResult;
    }

    public static RunSceneAction unmarshall(Map<String, Object> aSerializedData) {
        RunSceneAction theResult = new RunSceneAction();
        String theGameScene = (String) aSerializedData.get("gameScene");
        if (theGameScene != null) {
            theResult.gameScene.setQuietly(theGameScene);
        }
        return theResult;
    }
}