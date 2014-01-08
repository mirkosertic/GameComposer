package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ResourceName;

public interface Sprite {

    String RESOURCE_NAME_PROPERTY = "resourceName";
    String[] EDITABLE_PROPERTIES = {RESOURCE_NAME_PROPERTY};

    Property<ResourceName> resourceNameProperty();
}
