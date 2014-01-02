package de.mirkosertic.gameengine.sprites;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.ResourceName;

public interface Sprite {

    String RESOURCE_NAME_PROPERTY = "resourceName";

    Property<ResourceName> resourceNameProperty();
}
