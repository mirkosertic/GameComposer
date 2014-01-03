package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class PlayerScoreComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<PlayerScoreComponentTemplate> {

    @Override
    public String getTypeKey() {
        return PlayerScoreComponent.TYPE;
    }

    @Override
    public PlayerScoreComponentTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return PlayerScoreComponentTemplate.deserialize(aOwner, aEventmanager, aSerializedData);
    }
}
