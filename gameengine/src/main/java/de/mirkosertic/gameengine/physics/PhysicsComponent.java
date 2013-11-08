package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;

import java.util.HashMap;
import java.util.Map;

public class PhysicsComponent implements GameComponent {

    public static final String TYPE = "PhysicsComponent";

    private GameObjectInstance objectInstance;

    PhysicsComponent(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theStructure = new HashMap<String, Object>();
        theStructure.put(TYPE_ATTRIBUTE, TYPE);
        return theStructure;
    }

    @Override
    public CameraComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(CameraComponentTemplate.class);
    }

    public static PhysicsComponent deserialize(GameObjectInstance aObjectInstance, Map<String,Object> aSerializedData) {
        return new PhysicsComponent(aObjectInstance);
    }
}
