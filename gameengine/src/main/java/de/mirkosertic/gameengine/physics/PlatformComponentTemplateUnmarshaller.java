package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;

import java.util.Map;

public class PlatformComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<PlatformComponentTemplate> {

    @Override
    public String getTypeKey() {
        return PlatformComponent.TYPE;
    }

    @Override
    public PlatformComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        return PlatformComponentTemplate.deserialize(aSerializedData);
    }
}
