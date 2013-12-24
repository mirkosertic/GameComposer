package de.mirkosertic.gameengine.sprites;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.ResourceName;

public interface Sprite {

    Property<ResourceName> resourceNameProperty();
}
