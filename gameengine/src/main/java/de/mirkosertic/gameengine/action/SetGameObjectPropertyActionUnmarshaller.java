package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ActionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.IORegistry;

import java.util.Map;

public class SetGameObjectPropertyActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return SetGameObjectPropertyAction.TYPE_VALUE;
    }

    @Override
    public Action unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return SetGameObjectPropertyAction.unmarshall(aSerializedData, aGameScene);
    }
}
