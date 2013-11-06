package de.mirkosertic.gameengine.camera;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;

public class CameraComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<CameraComponentTemplate> {

    @Override
    public String getTypeKey() {
        return CameraComponent.TYPE;
    }

    @Override
    public CameraComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        return CameraComponentTemplate.deserialize(aOwner, aSerializedData);
    }
}
