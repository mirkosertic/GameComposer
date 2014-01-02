package de.mirkosertic.gameengine.android;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.type.ResourceName;

public class AndroidSoundResource implements GameResource {

    public final ResourceName resourceName;

    AndroidSoundResource(ResourceName aResourceName) {
        resourceName = aResourceName;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }
}
