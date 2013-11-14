package de.mirkosertic.gameengine.camera;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

public class CameraComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<CameraComponentTemplate> {

    @Override
    public String getTypeKey() {
        return CameraComponent.TYPE;
    }

    @Override
    public CameraComponentTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return CameraComponentTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
