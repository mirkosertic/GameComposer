package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.GameKeyCode;

public class PlatformClassInformation extends ClassInformation {

    public final static Field<Platform, Property<GameKeyCode>> MOVE_LEFT = new Field<Platform, Property<GameKeyCode>>("moveLeft", Property.class) {
        @Override
        public Property<GameKeyCode> getValue(Platform aObject) {
            return aObject.moveLeftKeyProperty();
        }
    };

    public final static Field<Platform, Property<GameKeyCode>> MOVE_RIGHT = new Field<Platform, Property<GameKeyCode>>("moveRight", Property.class) {
        @Override
        public Property<GameKeyCode> getValue(Platform aObject) {
            return aObject.moveRightKeyProperty();
        }
    };

    public final static Field<Platform, Property<GameKeyCode>> JUMPKEY = new Field<Platform, Property<GameKeyCode>>("jumpKey", Property.class) {
        @Override
        public Property<GameKeyCode> getValue(Platform aObject) {
            return aObject.jumpKeyProperty();
        }
    };

    public final static Field<Platform, Property<Float>> LEFTRIGHTIMPULSE = new Field<Platform, Property<Float>>("leftRightImpulse", Property.class) {
        @Override
        public Property<Float> getValue(Platform aObject) {
            return aObject.leftRightImpulseProperty();
        }
    };

    public final static Field<Platform, Property<Float>> JUMPIMPULSE = new Field<Platform, Property<Float>>("jumpImpulse", Property.class) {
        @Override
        public Property<Float> getValue(Platform aObject) {
            return aObject.jumpImpulseProperty();
        }
    };

    public static final PlatformClassInformation INSTANCE = new PlatformClassInformation();

    private PlatformClassInformation() {
        register(MOVE_LEFT);
        register(MOVE_RIGHT);
        register(JUMPKEY);
        register(LEFTRIGHTIMPULSE);
        register(JUMPIMPULSE);
    }
}
