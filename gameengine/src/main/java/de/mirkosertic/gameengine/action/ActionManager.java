package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEvent;

public class ActionManager implements GameSystem {

    private final GameScene scene;

    ActionManager(GameScene aScene) {
        scene = aScene;
    }

    @Override
    public void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
    }

    void onEvent(GameEvent aEvent) {
        for (EventSheet theSheet : scene.getEventSheets()) {
            for (GameRule theRule : theSheet.getRules()) {
                if (!theRule.conditionProperty().isNull()) {
                    if (theRule.conditionProperty().get().appliesTo(aEvent)) {
                        for (Action theAction : theRule.getActions()) {
                            theAction.invoke(scene);
                        }
                    }
                }
            }
        }
    }
}