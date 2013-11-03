package de.mirkosertic.gameengine.camera;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class CameraComponentTemplate implements GameComponentTemplate<CameraComponent> {

    public CameraComponentTemplate() {
    }

    public CameraComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        final CameraComponent theResult = new CameraComponent(aInstance);
        theResult.registerEvents(aGameRuntime);
        return theResult;
    }

    @Override
    public String getTypeKey() {
        return CameraComponent.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(CameraComponent.TYPE_ATTRIBUTE, CameraComponent.TYPE);
        return theResult;
    }

    public static CameraComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        return new CameraComponentTemplate();
    }
}
