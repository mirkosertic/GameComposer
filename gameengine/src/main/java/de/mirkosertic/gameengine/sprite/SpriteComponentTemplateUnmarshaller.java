package de.mirkosertic.gameengine.sprite;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

public class SpriteComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<SpriteComponentTemplate> {

    @Override
    public String getTypeKey() {
        return SpriteComponent.TYPE;
    }

    @Override
    public SpriteComponentTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return SpriteComponentTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
