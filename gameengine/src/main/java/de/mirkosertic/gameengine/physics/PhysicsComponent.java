package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.Property;

public class PhysicsComponent extends GameComponent {

    static final String TYPE = "PhysicsComponent";

    private final GameObjectInstance objectInstance;

    private final Property<Boolean> active;

    PhysicsComponent(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
        active = registerProperty(new Property<Boolean>(this, "active", Boolean.TRUE));
    }

    PhysicsComponent(GameObjectInstance aObjectInstance, PhysicsComponentTemplate aTemplate) {
        this(aObjectInstance);
        active.setQuietly(aTemplate.activeProperty().get());
    }

    public Property<Boolean> activeProperty() {
        return active;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theStructure = new HashMap<String, Object>();
        theStructure.put(TYPE_ATTRIBUTE, TYPE);
        theStructure.put("active", Boolean.toString(active.get()));
        return theStructure;
    }

    @Override
    public PhysicsComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(PhysicsComponentTemplate.class);
    }

    public static PhysicsComponent deserialize(GameObjectInstance aObjectInstance, Map<String,Object> aSerializedData) {
        PhysicsComponent theResult = new PhysicsComponent(aObjectInstance);
        String theActiveValue = (String) aSerializedData.get("active");
        if (theActiveValue != null) {
            theResult.active.setQuietly(Boolean.parseBoolean(theActiveValue));
        }
        return theResult;
    }
}
