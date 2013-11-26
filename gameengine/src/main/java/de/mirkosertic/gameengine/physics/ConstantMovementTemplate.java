package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.processes.StartProcess;
import de.mirkosertic.gameengine.types.Angle;

import java.util.HashMap;
import java.util.Map;

public class ConstantMovementTemplate extends GameComponentTemplate<ConstantMovement> {

    private final Property<Angle> movementDirection;
    private final Property<Float> movementForce;
    private final GameObject owner;

    public ConstantMovementTemplate(GameEventManager aEventManager, GameObject aOwner) {
        movementDirection = registerProperty(new Property<Angle>(this, "movementDirection", new Angle(0), aEventManager));
        movementForce = registerProperty(new Property<Float>(this, "movementForce", 0f, aEventManager));
        owner = aOwner;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public ConstantMovement create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        ConstantMovement theMovement = new ConstantMovement(aInstance, aGameRuntime, this);
        aGameRuntime.getEventManager().fire(new StartProcess(new ConstantMovementProcess(aGameRuntime, theMovement)));
        return theMovement;
    }

    @Override
    public String getTypeKey() {
        return ConstantMovement.TYPE;
    }

    public Property<Angle> movementDirectionProperty() {
        return movementDirection;
    }

    public Property<Float> movementForceProperty() {
        return movementForce;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(ConstantMovement.TYPE_ATTRIBUTE, ConstantMovement.TYPE);
        theResult.put("movementDirection", movementDirection.get().serialize());
        theResult.put("movementForce", Float.toString(movementForce.get()));
        return theResult;
    }

    public static ConstantMovementTemplate deserialize(GameEventManager aEventManager, GameObject aOwner, Map<String, Object> aSerializedData) {
        ConstantMovementTemplate theResult = new ConstantMovementTemplate(aEventManager, aOwner);
        Map<String, Object> theDirection = (Map<String, Object>) aSerializedData.get("movementDirection");
        if (theDirection != null) {
            theResult.movementDirection.setQuietly(Angle.deserialize(theDirection));
        }
        String theForce = (String) aSerializedData.get("movementForce");
        theResult.movementForce.setQuietly(Float.valueOf(theForce));
        return theResult;
    }
}
