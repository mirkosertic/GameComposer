package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.core.ActionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class KillProcessesForInstanceUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return KillProcessesForInstanceAction.TYPE_VALUE;
    }

    @Override
    public KillProcessesForInstanceAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return KillProcessesForInstanceAction.unmarshall();
    }
}
