package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.Property;

public interface Camera {

    String TYPE_PROPERTY = "type";

    @ReflectiveField
    Property<CameraType> typeProperty();

    @ReflectiveMethod
    void delete();
}
