package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToScene;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

public class NewGameInstance extends GameEvent implements DistributableEvent {

    public static final String EVENT_ID = "NewGameInstance";

    private final GameObjectInstance player;

    public NewGameInstance(GameObjectInstance aPlayer) {
        super(EVENT_ID);
        player = aPlayer;
    }

    @Override
    public boolean isForced() {
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, EVENT_ID);
        if (player != null) {
            GameObjectInstanceAddedToScene theEvent = new GameObjectInstanceAddedToScene(player);
            theResult.put("playerAddEvent", theEvent.serialize());
        }
        return theResult;
    }

    public static void runEventInScene(Map<String, Object> aEventData, GameScene aGameScene) {

        Map<String, Object> thePlayerAddEvent = (Map<String, Object>) aEventData.get("playerAddEvent");
        if (thePlayerAddEvent != null) {
            GameObjectInstanceAddedToScene.runEventInScene(thePlayerAddEvent, aGameScene);
        }

        NewGameInstance theInstance = new NewGameInstance(null);
        aGameScene.getRuntime().getEventManager().fire(theInstance);
    }
}
