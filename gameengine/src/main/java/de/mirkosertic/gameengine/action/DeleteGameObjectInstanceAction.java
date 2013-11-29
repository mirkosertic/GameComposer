package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.HashMap;
import java.util.Map;

public class DeleteGameObjectInstanceAction implements Action {

    public static final String TYPE_VALUE = "DeleteGameObjectInstanceAction";

    public DeleteGameObjectInstanceAction() {
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        for (GameObjectInstance theInstance : aResult.getAffectedInstances()) {
            aScene.removeGameObjectInstance(theInstance);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        return theResult;
    }

    public static DeleteGameObjectInstanceAction unmarshall(Map<String, Object> aSerializedData) {
        DeleteGameObjectInstanceAction theResult = new DeleteGameObjectInstanceAction();
        return theResult;
    }
}