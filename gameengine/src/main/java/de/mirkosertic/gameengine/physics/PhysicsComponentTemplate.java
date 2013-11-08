package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class PhysicsComponentTemplate implements GameComponentTemplate<PhysicsComponent> {

    private GameObject owner;
    private boolean fixedRotation;

    public PhysicsComponentTemplate(GameObject aOwner) {
        owner = aOwner;
        fixedRotation = false;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public PhysicsComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new PhysicsComponent(aInstance);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PhysicsComponent.TYPE_ATTRIBUTE, PhysicsComponent.TYPE);
        theResult.put("fixedrotation", Boolean.toString(fixedRotation));
        return theResult;
    }

    public static PhysicsComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        PhysicsComponentTemplate theTemplate = new PhysicsComponentTemplate(aOwner);
        String theFixedRotation = (String) aSerializedData.get("fixedrotation");
        if (theFixedRotation != null) {
            theTemplate.fixedRotation = Boolean.parseBoolean(theFixedRotation);
        }
        return theTemplate;
    }

    @Override
    public String getTypeKey() {
        return PhysicsComponent.TYPE;
    }
}