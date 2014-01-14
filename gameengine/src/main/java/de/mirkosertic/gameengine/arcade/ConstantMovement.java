package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Speed;

public interface ConstantMovement {

    String ANGLE_PROPERTY = "angle";
    String SPEED_PROPERTY = "angle";

    Property<Angle> angleProperty();

    Property<Speed> speedProperty();

}
