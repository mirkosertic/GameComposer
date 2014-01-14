package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.action.SystemTick;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.input.KeyPressed;
import de.mirkosertic.gameengine.input.KeyReleased;
import de.mirkosertic.gameengine.physic.GameObjectCollision;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Speed;

import java.util.HashMap;
import java.util.Map;

public class ConstantMovementBehavior implements Behavior, ConstantMovement, Reflectable<ConstantMovementClassInformation> {

    static final String TYPE = "ConstantMovement";

    private final GameObjectInstance objectInstance;

    private final Property<Angle> angle;
    private final Property<Speed> speed;

    ConstantMovementBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getComponentTemplate(ConstantMovementBehaviorTemplate.class));
    }

    ConstantMovementBehavior(GameObjectInstance aObjectInstance, ConstantMovementBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        angle = new Property<Angle>(Angle.class, this, ANGLE_PROPERTY, aTemplate.angleProperty().get(), theEventManager);
        speed = new Property<Speed>(Speed.class, this, SPEED_PROPERTY, aTemplate.speedProperty().get(), theEventManager);
    }

    public void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, SystemTick.class, new GameEventListener<SystemTick>() {
            public void handleGameEvent(SystemTick aEvent) {
                handleGameLoop(aEvent);
            }
        });
    }

    private void handleGameLoop(SystemTick aTick) {
    }


    @Override
    public ConstantMovementClassInformation getClassInformation() {
        return ConstantMovementClassInformation.INSTANCE;
    }

    @Override
    public ConstantMovementBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(ConstantMovementBehaviorTemplate.class);
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
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        theResult.put(ANGLE_PROPERTY, angle.get().serialize());
        theResult.put(SPEED_PROPERTY, speed.get().serialize());
        return theResult;
    }

    public static ConstantMovementBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        ConstantMovementBehavior theResult = new ConstantMovementBehavior(aObjectInstance);
        theResult.angle.setQuietly(Angle.deserialize((Map<String, Object>) aSerializedData.get(ANGLE_PROPERTY)));
        theResult.speed.setQuietly(Speed.deserialize((Map<String, Object>) aSerializedData.get(SPEED_PROPERTY)));
        theResult.registerEvents(aRuntime);
        return theResult;
    }
}
