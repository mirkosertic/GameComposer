package de.mirkosertic.gameengine.sprites;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;

public class SpriteComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<SpriteComponentTemplate> {

    @Override
    public String getTypeKey() {
        return SpriteComponent.TYPE;
    }

    @Override
    public SpriteComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        return SpriteComponentTemplate.deserialize(aSerializedData);
    }
}
