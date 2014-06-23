package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class RunSceneAction implements Action {

    public static final String TYPE_VALUE = "RunSceneAction";

    public static final String GAME_SCENE_PROPERTY = "gameScene";

    private final Property<String> gameScene;

    @UsedByReflection
    public RunSceneAction() {
        gameScene = new Property<>(String.class, this, GAME_SCENE_PROPERTY, (String) null);
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
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        if (gameScene.get() != null) {
            theResult.put(GAME_SCENE_PROPERTY, gameScene.get());
        }
        return theResult;
    }

    public static RunSceneAction unmarshall(Map<String, Object> aSerializedData) {
        RunSceneAction theResult = new RunSceneAction();
        String theGameScene = (String) aSerializedData.get(GAME_SCENE_PROPERTY);
        if (theGameScene != null) {
            theResult.gameScene.setQuietly(theGameScene);
        }
        return theResult;
    }
}