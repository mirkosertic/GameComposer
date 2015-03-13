package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToScene;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromScene;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.PropertyChanged;

import java.util.Map;

public class DefaultEventInterpreter implements EventInterpreter {

    @Override
    public void process(Map<String, Object> aEventData, GameScene aGameScene) {
        Object theEventID = aEventData.get(DistributableEvent.EVENT_ID_ATTRIBUTE);
        if (PropertyChanged.EVENT_ID.equals(theEventID)) {
            PropertyChanged.runEventInScene(aEventData, aGameScene);
            return;
        }
        if (GameObjectInstanceAddedToScene.EVENT_ID.equals(theEventID)) {
            GameObjectInstanceAddedToScene.runEventInScene(aEventData, aGameScene);
            return;
        }
        if (GameObjectInstanceRemovedFromScene.EVENT_ID.equals(theEventID)) {
            GameObjectInstanceRemovedFromScene.runEventInScene(aEventData, aGameScene);
            return;
        }
        if (NewGameInstance.EVENT_ID.equals(theEventID)) {
            NewGameInstance.runEventInScene(aEventData, aGameScene);
            return;
        }
    }
}