package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.GameComponentUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class CameraComponentUnmarshaller implements GameComponentUnmarshaller<CameraComponent> {

    @Override
    public String getTypeKey() {
        return CameraComponent.TYPE;
    }

    @Override
    public CameraComponent deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        CameraComponent theResult = CameraComponent.deserialize(aObjectInstance, aSerializedData);
        theResult.registerEvents(aRuntime);
        return theResult;
    }
}
