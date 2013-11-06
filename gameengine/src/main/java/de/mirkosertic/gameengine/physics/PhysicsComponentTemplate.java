package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class PhysicsComponentTemplate implements GameComponentTemplate<PhysicsComponent> {

    private GameObject owner;

    public PhysicsComponentTemplate(GameObject aOwner) {
        owner = aOwner;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public PhysicsComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new PhysicsComponent(aInstance);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PhysicsComponent.TYPE_ATTRIBUTE, PhysicsComponent.TYPE);        
        return theResult;
    }

    public static PhysicsComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        return new PhysicsComponentTemplate(aOwner);
    }

    @Override
    public String getTypeKey() {
        return PhysicsComponent.TYPE;
    }
}