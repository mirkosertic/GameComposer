package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.type.Angle;

import java.util.HashMap;
import java.util.Map;

public class ConstantMovementAction implements Action {

    public static final String TYPE_VALUE = "ConstantMovementAction";

    public static final String PROPERTY_MOVEMENT_DIRECTION = "movementDirection";
    public static final String PROPERTY_MOVEMENT_FORCE = "movementForce";

    private final Property<Angle> movementDirection;
    private final Property<Float> movementForce;

    @UsedByReflection
    public ConstantMovementAction() {
        movementDirection = new Property<Angle>(this, PROPERTY_MOVEMENT_DIRECTION, new Angle(0));
        movementForce = new Property<Float>(this, PROPERTY_MOVEMENT_FORCE, 0f);
    }

    public Property<Angle> movementDirectionProperty() {
        return movementDirection;
    }

    public Property<Float> movementForceProperty() {
        return movementForce;
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        for (GameObjectInstance theInstance : aResult.getAffectedInstances()) {
            ConstantMovementProcess theProcess = new ConstantMovementProcess(aScene.getRuntime(), theInstance, movementDirection.get(), movementForce.get());
            aScene.getRuntime().getEventManager().fire(new StartProcess(theProcess));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, ConstantMovementAction.TYPE_VALUE);
        theResult.put(PROPERTY_MOVEMENT_DIRECTION, movementDirection.get().serialize());
        theResult.put(PROPERTY_MOVEMENT_FORCE, Float.toString(movementForce.get()));
        return theResult;
    }

    public static ConstantMovementAction unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        ConstantMovementAction theResult = new ConstantMovementAction();
        Map<String, Object> theDirection = (Map<String, Object>) aSerializedData.get(PROPERTY_MOVEMENT_DIRECTION);
        if (theDirection != null) {
            theResult.movementDirection.setQuietly(Angle.deserialize(theDirection));
        }
        theResult.movementForce.setQuietly(Float.valueOf((String) aSerializedData.get(PROPERTY_MOVEMENT_FORCE)));
        return theResult;
    }
}
