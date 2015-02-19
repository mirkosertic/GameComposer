package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.type.DistributableUtils;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

@InheritedClassInformation
public class PropertyChanged extends GameEvent implements DistributableEvent {

    public static final String EVENT_ID = "PropertyChanged";

    private static final PropertyChangedClassInformation CIINSTANCE = new PropertyChangedClassInformation();

    @ReflectiveField
    public final Property property;

    @ReflectiveField
    public final Object oldValue;

    public PropertyChanged(Property aProperty, Object aOldValue) {
        super(EVENT_ID);
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
    public boolean isForced() {
        return false;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, type);
        theResult.put("propertyName", property.getName());
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

    public static void runEventInScene(Map<String, Object> aEventData, GameScene aGameScene) {
        String thePropertyName = (String ) aEventData.get("propertyName");
        String theInstanceID = (String) aEventData.get("instanceID");

        if (theInstanceID != null) {
            GameObjectInstance theInstance = aGameScene.findInstanceByID(theInstanceID);
            if (theInstance != null) {
                String theBehaviorType = (String) aEventData.get("behavior");
                if (theBehaviorType == null) {
                    DistributableUtils.setField(theInstance, thePropertyName, aEventData.get("newValue"));
                } else {
                    Object theBehavior = theInstance.findBehaviorByType(theBehaviorType);
                    if (theBehavior != null) {
                        // We KNOW every behavior is Reflectable
                        DistributableUtils.setField((Reflectable) theBehavior, thePropertyName, aEventData.get("newValue"));
                    }
                }
            }
        }
    }
}