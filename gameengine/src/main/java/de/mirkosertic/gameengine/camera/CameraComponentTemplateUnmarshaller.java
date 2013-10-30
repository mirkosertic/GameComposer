package de.mirkosertic.gameengine.camera;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;

public class CameraComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<CameraComponentTemplate> {

    @Override
    public String getTypeKey() {
        return CameraComponent.TYPE;
    }

    @Override
    public CameraComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        return CameraComponentTemplate.deserialize(aSerializedData);
    }
}
