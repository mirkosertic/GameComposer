package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.type.DistributableUtils;

import java.util.HashMap;
import java.util.Map;

@InheritedClassInformation
public class PropertyChanged extends GameEvent implements DistributableEvent {

    private static final PropertyChangedClassInformation CIINSTANCE = new PropertyChangedClassInformation();

    @ReflectiveField
    public final Property property;

    @ReflectiveField
    public final Object oldValue;

    public PropertyChanged(Property aProperty, Object aOldValue) {
        super("PropertyChanged");
        property = aProperty;
        oldValue = aOldValue;
    }

    @ReflectiveMethod
    public Object getOwner() {
        return property.getOwner();
    }

    @Override
    public PropertyChangedClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, type);
        theResult.put("propertyName", property.getName());
        theResult.put("oldValue", DistributableUtils.convert(oldValue));
        theResult.put("newValue", DistributableUtils.convert(property.get()));

        Object theOwner = property.getOwner();
        if (theOwner instanceof GameObjectInstance) {
            GameObjectInstance theInstance = (GameObjectInstance) theOwner;
            theResult.put("instanceID", theInstance.uuidProperty().get());
        }
        if (theOwner instanceof Behavior) {
            Behavior theBehavior = (Behavior) theOwner;
            theResult.put("instanceID", theBehavior.getInstance().uuidProperty().get());
            theResult.put("behavior", theBehavior.getType());
        }

        return theResult;
    }
}