package de.mirkosertic.gameengine.script;

import de.mirkosertic.gameengine.core.ActionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class RunScriptActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return RunScriptAction.TYPE_VALUE;
    }

    @Override
    public RunScriptAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return RunScriptAction.unmarshall(aSerializedData);
    }
}