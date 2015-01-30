package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

@InheritedClassInformation
public class PropertyChanged extends GameEvent {

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
}