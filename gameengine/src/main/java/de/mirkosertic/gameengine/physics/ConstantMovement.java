package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.Angle;

import java.util.HashMap;
import java.util.Map;

public class ConstantMovement extends GameComponent {

    static final String TYPE = "ConstantMovementComponent";

    private final GameObjectInstance objectInstance;
    private final Property<Angle> movementDirection;
    private final Property<Float> movementForce;

    public ConstantMovement(GameObjectInstance aInstance, GameRuntime aRuntime, ConstantMovementTemplate aTemplate) {
        objectInstance = aInstance;
        movementDirection = registerProperty(new Property<Angle>(this, "movementDirection", aTemplate.movementDirectionProperty().get()));
        movementForce = registerProperty(new Property<Float>(this, "movementForce", aTemplate.movementForceProperty().get()));
    }

    public GameObjectInstance getObjectInstance() {
        return objectInstance;
    }

    public Property<Angle> movementDirectionProperty() {
        return movementDirection;
    }

    public Property<Float> movementForceProperty() {
        return movementForce;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public ConstantMovementTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(ConstantMovementTemplate.class);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        return theResult;
    }

    public static ConstantMovement deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        ConstantMovementTemplate theTemplate = aObjectInstance.getOwnerGameObject().getComponentTemplate(ConstantMovementTemplate.class);
        return new ConstantMovement(aObjectInstance, aRuntime, theTemplate);
    }
}
