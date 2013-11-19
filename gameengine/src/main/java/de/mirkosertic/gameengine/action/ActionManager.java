package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEvent;

public class ActionManager implements GameSystem {

    private final GameScene scene;

    ActionManager(GameScene aScene) {
        scene = aScene;
    }

    void onEvent(GameEvent aEvent) {
        for (EventSheet theSheet : scene.getEventSheets()) {
            for (GameRule theRule : theSheet.getRules()) {
                if (!theRule.conditionProperty().isNull()) {
                    ConditionResult theResult = theRule.conditionProperty().get().appliesTo(aEvent);
                    if (theResult.isConditionTrue()) {
                        for (Action theAction : theRule.getActions()) {
                            theAction.invoke(scene, theResult);
                        }
                    }
                }
            }
        }
    }
}