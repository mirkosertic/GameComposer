package de.mirkosertic.gameengine.android;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

public class AndroidSoundResource implements GameResource {

    private final byte[] data;

    AndroidSoundResource(byte[] aData) {
        data = aData;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }
}
