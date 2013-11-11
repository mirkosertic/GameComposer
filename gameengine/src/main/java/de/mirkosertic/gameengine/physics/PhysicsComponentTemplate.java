package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

public class PhysicsComponentTemplate implements GameComponentTemplate<PhysicsComponent> {

    private GameObject owner;
    
    private Property<Boolean> fixedRotation;

    public PhysicsComponentTemplate(GameObject aOwner) {
        GameEventManager theManager = aOwner.getGameScene().getRuntime().getEventManager();
        owner = aOwner;
        
        fixedRotation = new Property<Boolean>(this, "fixedRotation", Boolean.FALSE, theManager);
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }
    
    public Property<Boolean> fixedRotationProperty() {
        return fixedRotation;
    }

    public PhysicsComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new PhysicsComponent(aInstance);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PhysicsComponent.TYPE_ATTRIBUTE, PhysicsComponent.TYPE);
        theResult.put("fixedrotation", Boolean.toString(fixedRotation.get()));
        return theResult;
    }

    public static PhysicsComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        PhysicsComponentTemplate theTemplate = new PhysicsComponentTemplate(aOwner);
        String theFixedRotation = (String) aSerializedData.get("fixedrotation");
        if (theFixedRotation != null) {
            theTemplate.fixedRotation.setQuietly(Boolean.parseBoolean(theFixedRotation));
        }
        return theTemplate;
    }

    @Override
    public String getTypeKey() {
        return PhysicsComponent.TYPE;
    }
}
