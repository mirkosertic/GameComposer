package de.mirkosertic.gameengine.android;

import android.content.res.AssetFileDescriptor;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.type.ResourceName;

public class AndroidSoundResource implements GameResource {

    public final AssetFileDescriptor fileDescriptor;
    public final ResourceName resourceName;

    AndroidSoundResource(ResourceName aResourceName, AssetFileDescriptor aFileDescriptor) {
        fileDescriptor = aFileDescriptor;
        resourceName = aResourceName;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }
}
