package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

public class NewGameInstance extends GameEvent implements DistributableEvent {

    public static final String EVENT_ID = "NewGameInstance";

    public NewGameInstance() {
        super(EVENT_ID);
    }

    @Override
    public boolean isForced() {
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, EVENT_ID);
        return theResult;
    }

    public static void runEventInScene(Map<String, Object> aEventData, GameScene aGameScene) {
        NewGameInstance theInstance = new NewGameInstance();
        aGameScene.getRuntime().getEventManager().fire(theInstance);
    }
}
