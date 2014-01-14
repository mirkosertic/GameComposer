package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Speed;

import java.util.HashMap;
import java.util.Map;

public class ConstantMovementBehaviorTemplate implements BehaviorTemplate<ConstantMovementBehavior>, ConstantMovement, Reflectable<ConstantMovementClassInformation> {

    private final Property<Angle> angle;
    private final Property<Speed> speed;
    private final GameObject owner;

    @UsedByReflection
    public ConstantMovementBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        angle = new Property<Angle>(Angle.class, this, ANGLE_PROPERTY, new Angle(0), aEventManager);
        speed = new Property<Speed>(Speed.class, this, SPEED_PROPERTY, new Speed(0), aEventManager);
        owner = aOwner;
    }

    @Override
    public ConstantMovementClassInformation getClassInformation() {
        return ConstantMovementClassInformation.INSTANCE;
    }

    @Override
    public ConstantMovementBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        ConstantMovementBehavior theResult = new ConstantMovementBehavior(aInstance);
        theResult.registerEvents(aGameRuntime);
        return theResult;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public Property<Angle> angleProperty() {
        return angle;
    }

    @Override
    public Property<Speed> speedProperty() {
        return speed;
    }

    @Override
    public String getTypeKey() {
        return ConstantMovementBehavior.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(ConstantMovementBehavior.TYPE_ATTRIBUTE, ConstantMovementBehavior.TYPE);
        theResult.put(ANGLE_PROPERTY, angle.get().serialize());
        theResult.put(SPEED_PROPERTY, speed.get().serialize());
        return theResult;
    }

    public static ConstantMovementBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        ConstantMovementBehaviorTemplate theResult = new ConstantMovementBehaviorTemplate(aEventmanager, aOwner);
        theResult.angle.setQuietly(Angle.deserialize((Map<String, Object>) aSerializedData.get(ANGLE_PROPERTY)));
        theResult.speed.setQuietly(Speed.deserialize((Map<String, Object>) aSerializedData.get(SPEED_PROPERTY)));
        return theResult;
    }
}
