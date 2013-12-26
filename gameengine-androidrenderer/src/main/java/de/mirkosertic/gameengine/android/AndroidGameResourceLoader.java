package de.mirkosertic.gameengine.android;

import android.content.res.AssetManager;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.types.ResourceName;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class AndroidGameResourceLoader implements GameResourceLoader {

    private final AssetManager assetManager;

    public AndroidGameResourceLoader(AssetManager aAssetManager) {
        assetManager = aAssetManager;
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        if (aResourceName.name.endsWith(".png")) {
            InputStream theStream = assetManager.open(aResourceName.name);
            byte[] theData = IOUtils.toByteArray(theStream);
            return new AndroidBitmapResource(theData);
        }
        if (aResourceName.name.endsWith(".wav")) {
            InputStream theStream = assetManager.open(aResourceName.name);
            byte[] theData = IOUtils.toByteArray(theStream);
            return new AndroidSoundResource(theData);
        }

        return null;

    }

    @Override
    public void flush() {
    }
}
