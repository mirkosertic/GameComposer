package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.core.BehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class PlayerScoreBehaviorTemplateUnmarshaller implements BehaviorTemplateUnmarshaller<PlayerScoreBehaviorTemplate> {

    @Override
    public String getTypeKey() {
        return PlayerScoreBehavior.TYPE;
    }

    @Override
    public PlayerScoreBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return PlayerScoreBehaviorTemplate.deserialize(aOwner, aEventmanager, aSerializedData);
    }
}
