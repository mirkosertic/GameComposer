package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.HashMap;
import java.util.Map;

public class KillProcessesForInstanceAction implements Action {

    public static final String TYPE_VALUE = "KillProcessesForInstanceAction";

    @UsedByReflection
    public KillProcessesForInstanceAction() {
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        GameEventManager theEventManager = aScene.getRuntime().getEventManager();
        for (GameObjectInstance theInstance : aResult.getAffectedInstances()) {
            theEventManager.fire(new KillProcessesForInstance(theInstance));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        return theResult;
    }

    public static KillProcessesForInstanceAction unmarshall() {
        return new KillProcessesForInstanceAction();
    }
}
