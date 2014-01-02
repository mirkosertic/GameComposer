package de.mirkosertic.gameengine.physic;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

public class PhysicsComponent extends GameComponent implements Physics {

    static final String TYPE = "PhysicsComponent";

    private final GameObjectInstance objectInstance;

    private final Property<Boolean> active;
    private final Property<Boolean> fixedRotation;
    private final Property<Float> density;
    private final Property<Float> friction;
    private final Property<Float> restitution;
    private final Property<Float> gravityScale;

    PhysicsComponent(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getComponentTemplate(PhysicsComponentTemplate.class));
    }

    PhysicsComponent(GameObjectInstance aObjectInstance, PhysicsComponentTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        active = registerProperty(new Property<Boolean>(this, ACTIVE_PROPERTY, aTemplate.activeProperty().get(), theEventManager));
        fixedRotation = registerProperty(new Property<Boolean>(this, FIXED_ROTATION_PROPERTY, aTemplate.fixedRotationProperty().get(), theEventManager));
        density = registerProperty(new Property<Float>(this, DENSITY_PROPERTY, aTemplate.densityProperty().get(), theEventManager));
        friction = registerProperty(new Property<Float>(this, FRICTION_PROPERTY, aTemplate.frictionProperty().get(), theEventManager));
        restitution = registerProperty(new Property<Float>(this, RESTITUTION_PROPERTY, aTemplate.restitutionProperty().get(), theEventManager));
        gravityScale = registerProperty(new Property<Float>(this, GRAVITY_SCALE_PROPERTY, aTemplate.gravityScaleProperty().get(), theEventManager));
    }

    public Property<Boolean> activeProperty() {
        return active;
    }

    public Property<Boolean> fixedRotationProperty() {
        return fixedRotation;
    }

    public Property<Float> densityProperty() {
        return density;
    }

    public Property<Float> frictionProperty() {
        return friction;
    }

    public Property<Float> restitutionProperty() {
        return restitution;
    }

    public Property<Float> gravityScaleProperty() {
        return gravityScale;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theStructure = new HashMap<String, Object>();
        theStructure.put(TYPE_ATTRIBUTE, TYPE);
        theStructure.put(ACTIVE_PROPERTY, Boolean.toString(active.get()));
        return theStructure;
    }

    @Override
    public PhysicsComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(PhysicsComponentTemplate.class);
    }

    public static PhysicsComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        PhysicsComponent theResult = new PhysicsComponent(aObjectInstance);
        String theActiveValue = (String) aSerializedData.get(ACTIVE_PROPERTY);
        if (theActiveValue != null) {
            theResult.active.setQuietly(Boolean.parseBoolean(theActiveValue));
        }
        return theResult;
    }
}
