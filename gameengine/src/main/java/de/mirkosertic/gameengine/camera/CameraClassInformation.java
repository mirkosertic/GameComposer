package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;

public class CameraClassInformation extends ClassInformation {

    public static final CameraClassInformation INSTANCE = new CameraClassInformation();

    public final static Field<Camera, Property<CameraType>> TYPE = new Field<Camera, Property<CameraType>>("type", Property.class) {
        @Override
        public Property<CameraType> getValue(Camera aObject) {
            return aObject.typeProperty();
        }
    };

    private CameraClassInformation() {
        register(TYPE);
    }
}
