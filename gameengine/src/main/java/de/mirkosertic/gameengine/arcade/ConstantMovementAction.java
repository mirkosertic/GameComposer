package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.processes.StartProcess;
import de.mirkosertic.gameengine.types.Angle;

import java.util.HashMap;
import java.util.Map;

public class ConstantMovementAction implements Action {

    public static final String TYPE_VALUE = "ConstantMovementAction";

    private final Property<Angle> movementDirection;
    private final Property<Float> movementForce;

    @UsedByReflection
    public ConstantMovementAction() {
        movementDirection = new Property<Angle>(this, "movementDirection", new Angle(0));
        movementForce = new Property<Float>(this, "movementForce", 0f);
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
        theResult.put("movementDirection", movementDirection.get().serialize());
        theResult.put("movementForce", Float.toString(movementForce.get()));
        return theResult;
    }

    public static ConstantMovementAction unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        ConstantMovementAction theResult = new ConstantMovementAction();
        Map<String, Object> theDirection = (Map<String, Object>) aSerializedData.get("movementDirection");
        if (theDirection != null) {
            theResult.movementDirection.setQuietly(Angle.deserialize(theDirection));
        }
        theResult.movementForce.setQuietly(Float.valueOf((String) aSerializedData.get("movementForce")));
        return theResult;
    }
}
