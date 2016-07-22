package de.mirkosertic.gameengine.core;

import java.util.Map;

public class SpawnGameObjectInstanceActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return SpawnGameObjectInstanceAction.TYPE_VALUE;
    }

    @Override
    public Action unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return SpawnGameObjectInstanceAction.unmarshall(aSerializedData, aGameScene);
    }
}
