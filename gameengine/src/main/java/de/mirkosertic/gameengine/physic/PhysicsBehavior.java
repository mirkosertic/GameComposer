package de.mirkosertic.gameengine.physic;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;

public class PhysicsBehavior implements Behavior, Physics, Reflectable<PhysicsClassInformation> {

    static final String TYPE = "Physics";

    private final GameObjectInstance objectInstance;

    private final Property<Boolean> active;
    private final Property<Boolean> fixedRotation;
    private final Property<Float> density;
    private final Property<Float> friction;
    private final Property<Float> restitution;
    private final Property<Float> gravityScale;

    PhysicsBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getComponentTemplate(PhysicsBehaviorTemplate.class));
    }

    PhysicsBehavior(GameObjectInstance aObjectInstance, PhysicsBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        active = new Property<Boolean>(Boolean.class, this, ACTIVE_PROPERTY, aTemplate.activeProperty().get(), theEventManager);
        fixedRotation = new Property<Boolean>(Boolean.class, this, FIXED_ROTATION_PROPERTY, aTemplate.fixedRotationProperty().get(), theEventManager);
        density = new Property<Float>(Float.class, this, DENSITY_PROPERTY, aTemplate.densityProperty().get(), theEventManager);
        friction = new Property<Float>(Float.class, this, FRICTION_PROPERTY, aTemplate.frictionProperty().get(), theEventManager);
        restitution = new Property<Float>(Float.class, this, RESTITUTION_PROPERTY, aTemplate.restitutionProperty().get(), theEventManager);
        gravityScale = new Property<Float>(Float.class, this, GRAVITY_SCALE_PROPERTY, aTemplate.gravityScaleProperty().get(), theEventManager);
    }

    @Override
    public PhysicsClassInformation getClassInformation() {
        return PhysicsClassInformation.INSTANCE;
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
    public PhysicsBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(PhysicsBehaviorTemplate.class);
    }

    public static PhysicsBehavior deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        PhysicsBehavior theResult = new PhysicsBehavior(aObjectInstance);
        String theActiveValue = (String) aSerializedData.get(ACTIVE_PROPERTY);
        if (theActiveValue != null) {
            theResult.active.setQuietly(Boolean.parseBoolean(theActiveValue));
        }
        return theResult;
    }
}
