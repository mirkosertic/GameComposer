package de.mirkosertic.gameengine.core;

import java.util.Map;

public class DeleteGameObjectInstanceActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return DeleteGameObjectInstanceAction.TYPE_VALUE;
    }

    @Override
    public Action unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return DeleteGameObjectInstanceAction.unmarshall();
    }
}
