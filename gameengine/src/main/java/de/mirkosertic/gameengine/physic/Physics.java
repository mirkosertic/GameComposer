package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.event.Property;

public interface Physics {

    String ACTIVE_PROPERTY = "active";
    String FIXED_ROTATION_PROPERTY = "fixedRotation";
    String DENSITY_PROPERTY = "density";
    String FRICTION_PROPERTY = "friction";
    String RESTITUTION_PROPERTY = "restitution";
    String GRAVITY_SCALE_PROPERTY = "gravityScale";

    Property<Boolean> activeProperty();

    Property<Boolean> fixedRotationProperty();

    Property<Float> densityProperty();

    Property<Float> frictionProperty();

    Property<Float> restitutionProperty();

    Property<Float> gravityScaleProperty();
}
