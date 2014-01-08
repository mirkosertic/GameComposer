package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.event.Property;

public interface Camera {

    String TYPE_PROPERTY = "type";
    String[] EDITABLE_PROPERTIES = {TYPE_PROPERTY};

    Property<CameraType> typeProperty();
}
