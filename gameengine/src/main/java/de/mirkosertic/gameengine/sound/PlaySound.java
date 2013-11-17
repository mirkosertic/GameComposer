package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class PlaySound extends GameEvent {

    private final Property<ResourceName> resourceName;

    public PlaySound(ResourceName aResourceName) {
        super("PlaySound");
        resourceName = registerProperty(new Property<ResourceName>(this, "resourceName", aResourceName));
    }

    public ReadOnlyProperty<ResourceName> resourceNameProperty() {
        return resourceName;
    }
}
