package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.event.Property;

public interface Camera {

    String TYPE_PROPERTY = "type";

    Property<CameraType> typeProperty();

    void delete();
}
