package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Speed;

public class ConstantMovementClassInformation extends ClassInformation {

    public final static Field<ConstantMovement, Property<Speed>> SPEED = new Field<ConstantMovement, Property<Speed>>("speed", Property.class) {
        @Override
        public Property<Speed> getValue(ConstantMovement aObject) {
            return aObject.speedProperty();
        }
    };

    public final static Field<ConstantMovement, Property<Speed>> ROTATIONSPEED = new Field<ConstantMovement, Property<Speed>>("rotationSpeed", Property.class) {
        @Override
        public Property<Speed> getValue(ConstantMovement aObject) {
            return aObject.rotationSpeedProperty();
        }
    };

    public static final ConstantMovementClassInformation INSTANCE = new ConstantMovementClassInformation();

    private ConstantMovementClassInformation() {
        register(SPEED);
        register(ROTATIONSPEED);
    }
}
