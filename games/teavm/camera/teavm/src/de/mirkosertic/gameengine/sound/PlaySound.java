package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.ResourceName;

@InheritedClassInformation
class PlaySound extends GameEvent {

    private static final PlaySoundClassInformation CIINSTANCE = new PlaySoundClassInformation();

    @ReflectiveField
    public final ResourceName resourceName;

    public PlaySound(ResourceName aResourceName) {
        super("PlaySound");
        resourceName = aResourceName;
    }

    @Override
    public PlaySoundClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}