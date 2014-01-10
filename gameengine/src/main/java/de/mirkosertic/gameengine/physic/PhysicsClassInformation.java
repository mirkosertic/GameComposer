package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;

public class PhysicsClassInformation extends ClassInformation {

    public final static Field<Physics, Property<Boolean>> ACTIVE = new Field<Physics, Property<Boolean>>("active", Property.class) {
        @Override
        public Property<Boolean> getValue(Physics aObject) {
            return aObject.activeProperty();
        }
    };

    public final static Field<Physics, Property<Boolean>> FIXEDROTATION = new Field<Physics, Property<Boolean>>("fixedRotation", Property.class) {
        @Override
        public Property<Boolean> getValue(Physics aObject) {
            return aObject.fixedRotationProperty();
        }
    };

    public final static Field<Physics, Property<Float>> DENSITY = new Field<Physics, Property<Float>>("density", Property.class) {
        @Override
        public Property<Float> getValue(Physics aObject) {
            return aObject.densityProperty();
        }
    };

    public final static Field<Physics, Property<Float>> FRICTION = new Field<Physics, Property<Float>>("friction", Property.class) {
        @Override
        public Property<Float> getValue(Physics aObject) {
            return aObject.frictionProperty();
        }
    };

    public final static Field<Physics, Property<Float>> RESTITUTION = new Field<Physics, Property<Float>>("restitution", Property.class) {
        @Override
        public Property<Float> getValue(Physics aObject) {
            return aObject.restitutionProperty();
        }
    };

    public final static Field<Physics, Property<Float>> GRAVITYSCALE = new Field<Physics, Property<Float>>("gravityScale", Property.class) {
        @Override
        public Property<Float> getValue(Physics aObject) {
            return aObject.gravityScaleProperty();
        }
    };

    public static final PhysicsClassInformation INSTANCE = new PhysicsClassInformation();

    private PhysicsClassInformation() {
        register(ACTIVE);
        register(FIXEDROTATION);
        register(DENSITY);
        register(FRICTION);
        register(RESTITUTION);
        register(GRAVITYSCALE);
    }
}
