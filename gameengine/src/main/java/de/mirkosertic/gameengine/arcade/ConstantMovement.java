package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Speed;

public interface ConstantMovement {

    String SPEED_PROPERTY = "speed";
    String ROTATIONSPEED_PROPERTY = "speed";

    Property<Speed> speedProperty();

    Property<Speed> rotationSpeedProperty();

}
