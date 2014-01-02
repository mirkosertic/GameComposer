package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.ResourceName;

class PlaySound extends GameEvent {

    public final ResourceName resourceName;

    public PlaySound(ResourceName aResourceName) {
        super("PlaySound");
        resourceName = aResourceName;
    }
}
