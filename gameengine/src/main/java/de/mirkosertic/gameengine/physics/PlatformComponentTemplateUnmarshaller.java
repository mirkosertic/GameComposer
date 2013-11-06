package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;

import java.util.Map;

public class PlatformComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<PlatformComponentTemplate> {

    @Override
    public String getTypeKey() {
        return PlatformComponent.TYPE;
    }

    @Override
    public PlatformComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        return PlatformComponentTemplate.deserialize(aOwner, aSerializedData);
    }
}
