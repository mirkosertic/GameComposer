package de.mirkosertic.gameengine.android;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

public class AndroidBitmapResource implements GameResource {

    private final byte[] data;

    AndroidBitmapResource(byte[] aData) {
        data = aData;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.BITMAP;
    }
}
