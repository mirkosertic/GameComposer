package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Speed;

public interface ConstantMovement {

    String SPEED_PROPERTY = "speed";
    String ROTATIONSPEED_PROPERTY = "rotationSpeed";

    @ReflectiveField
    Property<Speed> speedProperty();

    @ReflectiveField
    Property<Speed> rotationSpeedProperty();

    @ReflectiveMethod
    void delete();
}
