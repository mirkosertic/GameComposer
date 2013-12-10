package de.mirkosertic.gameengine.core;

import java.util.Map;

public class RunSceneActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return RunSceneAction.TYPE_VALUE;
    }

    @Override
    public RunSceneAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return RunSceneAction.unmarshall(aSerializedData);
    }
}
