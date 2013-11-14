package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class PlaySoundEvent extends GameEvent {

    private final ReadOnlyProperty<ResourceName> resourceName;

    public PlaySoundEvent(ResourceName aResourceName) {
        super("PlaySoundEvent");
        resourceName = registerProperty(new ReadOnlyProperty<ResourceName>(this, "resourceName", aResourceName));
    }

    public ReadOnlyProperty<ResourceName> resourceNameProperty() {
        return resourceName;
    }
}
