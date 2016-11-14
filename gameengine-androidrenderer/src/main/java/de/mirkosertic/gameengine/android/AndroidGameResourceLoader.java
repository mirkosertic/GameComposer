/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.android;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.File;
import java.io.FileOutputStream;
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
    public Promise<GameResource, String> load(final ResourceName aResourceName) {
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
                try {
                    if (aResourceName.name.endsWith(".png")) {
                        InputStream theStream = assetManager.open(prefix + aResourceName.name);
                        aResolver.resolve(new AndroidBitmapResource(BitmapFactory.decodeStream(theStream)));
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
                        aResolver.resolve(new AndroidSoundResource(new ResourceName(prefix + aResourceName.name), theFileDescriptor));
                    }
                } catch (Exception e) {
                    aRejector.reject(e.getMessage(), e);
                }
            }
        });
    }

    @Override
    public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
                aResolver.resolve(LoadedSpriteSheet.EMPTY);
            }
        });
    }

    @Override
    public void flush() {
    }
}