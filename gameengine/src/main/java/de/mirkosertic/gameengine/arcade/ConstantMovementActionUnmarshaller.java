package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ActionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class ConstantMovementActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return ConstantMovementAction.TYPE_VALUE;
    }

    @Override
    public Action unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return ConstantMovementAction.unmarshall(aGameScene, aSerializedData);
    }
}
