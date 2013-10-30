package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;

public class PhysicsComponent implements GameComponent {

    public static final String TYPE = "PhysicsComponent";

    private GameObjectInstance objectInstance;

    PhysicsComponent(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theStructure = new HashMap<>();
        theStructure.put(TYPE_ATTRIBUTE, TYPE);
        return theStructure;
    }

    public static PhysicsComponent deserialize(GameObjectInstance aObjectInstance, Map<String,Object> aSerializedData) {
        return new PhysicsComponent(aObjectInstance);
    }
}
