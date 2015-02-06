package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ActionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class PlaySoundActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return PlaySoundAction.TYPE_VALUE;
    }

    @Override
    public Action unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        return PlaySoundAction.unmarshall(aSerializedData);
    }
}
