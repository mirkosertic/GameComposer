package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class PhysicsComponentTemplate implements GameComponentTemplate<PhysicsComponent> {

    public PhysicsComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new PhysicsComponent(aInstance);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PhysicsComponent.TYPE_ATTRIBUTE, PhysicsComponent.TYPE);        
        return theResult;
    }

    public static PhysicsComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        return new PhysicsComponentTemplate();
    }

    @Override
    public String getTypeKey() {
        return PhysicsComponent.TYPE;
    }
}