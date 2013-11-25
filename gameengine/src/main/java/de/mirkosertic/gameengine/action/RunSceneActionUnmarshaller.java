package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.ActionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

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
