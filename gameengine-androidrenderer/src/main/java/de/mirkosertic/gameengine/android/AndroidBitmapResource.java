package de.mirkosertic.gameengine.android;

import android.graphics.Bitmap;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

public class AndroidBitmapResource implements GameResource {

    public final Bitmap bitmap;

    AndroidBitmapResource(Bitmap aBitmap) {
        bitmap = aBitmap;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.BITMAP;
    }
}
