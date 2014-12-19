package de.mirkosertic.gameengine.android;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AndroidGameResourceLoader implements GameResourceLoader {

    private final AssetManager assetManager;
    private final String prefix;
    private final File cacheDir;

    public AndroidGameResourceLoader(AssetManager aAssetManager, File aCacheDir, String aSceneID) {
        assetManager = aAssetManager;
        prefix = aSceneID;
        cacheDir = aCacheDir;
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        if (aResourceName.name.endsWith(".png")) {
            InputStream theStream = assetManager.open(prefix + aResourceName.name);
            return new AndroidBitmapResource(BitmapFactory.decodeStream(theStream));
        }
        if (aResourceName.name.endsWith(".wav")) {
            File theCachedFile = new File(cacheDir, prefix + aResourceName.name);
            // Don's forget to create the directory before writing :-)
            theCachedFile.getParentFile().mkdirs();
            InputStream theInput = assetManager.open(prefix + aResourceName.name);
            FileOutputStream theFos = new FileOutputStream(theCachedFile);
            byte[] theCache = new byte[8192];
            int theRead = theInput.read(theCache);
            while(theRead >  0) {
                theFos.write( theCache,0, theRead);
                theRead = theInput.read(theCache);
            }
            theFos.close();
            AssetFileDescriptor theFileDescriptor = new AssetFileDescriptor(ParcelFileDescriptor.open(theCachedFile, ParcelFileDescriptor.MODE_READ_ONLY ), 0, -1);
            return new AndroidSoundResource(new ResourceName(prefix + aResourceName.name), theFileDescriptor);
        }

        return null;

    }

    @Override
    public void flush() {
    }
}