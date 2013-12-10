package de.mirkosertic.gameengine.core;

import java.util.Map;

public class SetPropertyActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return SetPropertyAction.TYPE_VALUE;
    }

    @Override
    public Action unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return SetPropertyAction.unmarshall(aSerializedData, aGameScene);
    }
}
