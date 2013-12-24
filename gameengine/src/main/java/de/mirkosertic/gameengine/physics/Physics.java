package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.Property;

public interface Physics {

    Property<Boolean> activeProperty();

    Property<Boolean> fixedRotationProperty();

    Property<Float> densityProperty();

    Property<Float> frictionProperty();

    Property<Float> restitutionProperty();

    Property<Float> gravityScaleProperty();
}
