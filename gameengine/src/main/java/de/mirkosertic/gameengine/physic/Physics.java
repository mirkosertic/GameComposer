package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.Property;

public interface Physics {

    String ACTIVE_PROPERTY = "active";
    String FIXED_ROTATION_PROPERTY = "fixedRotation";
    String DENSITY_PROPERTY = "density";
    String FRICTION_PROPERTY = "friction";
    String RESTITUTION_PROPERTY = "restitution";
    String GRAVITY_SCALE_PROPERTY = "gravityScale";

    @ReflectiveField
    Property<Boolean> activeProperty();

    @ReflectiveField
    Property<Boolean> fixedRotationProperty();

    @ReflectiveField
    Property<Float> densityProperty();

    @ReflectiveField
    Property<Float> frictionProperty();

    @ReflectiveField
    Property<Float> restitutionProperty();

    @ReflectiveField
    Property<Float> gravityScaleProperty();

    @ReflectiveMethod
    void delete();
}
