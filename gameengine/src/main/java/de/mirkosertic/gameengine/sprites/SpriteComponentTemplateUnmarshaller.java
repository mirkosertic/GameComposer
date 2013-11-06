package de.mirkosertic.gameengine.sprites;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;

public class SpriteComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<SpriteComponentTemplate> {

    @Override
    public String getTypeKey() {
        return SpriteComponent.TYPE;
    }

    @Override
    public SpriteComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        return SpriteComponentTemplate.deserialize(aOwner, aSerializedData);
    }
}
