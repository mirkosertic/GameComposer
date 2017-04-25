/*
 * Copyright 2017 Mirko Sertic
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
package de.mirkosertic.gameengine.teavmwasm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.UUID;
import org.teavm.interop.Export;
import org.teavm.interop.Import;

import java.util.HashMap;
import java.util.Map;

public class WASMGameResourceLoader implements GameResourceLoader {

    private static final Map<String, Promise<Long, Long>> PROMISES = new HashMap();

    @Export(name = "resolvePromiseWithID")
    public static void resolvePromiseWithID(String aPromiseID, int aReason) {
        Promise<Long, Long> thePromise = PROMISES.remove(aPromiseID);
        thePromise.resolve(Long.valueOf(aReason));
    }

    private static Map<String, WASMLoadedSpriteSheet> LOADINGSHEETS = new HashMap<>();

    private final String sceneId;

    @Import(module = "engine", name = "loadSpriteSheet")
    public static native void loadSpriteSheet(String aPromiseID, String aResourceName);

    @Export(name = "registerResourceToSpriteSheet")
    public static void registerResourceToSpriteSheet(String aSpriteSheet, int aStringPoolResourceName, int aExternalResourceID) {
        WASMLoadedSpriteSheet theSheet = LOADINGSHEETS.get(aSpriteSheet);
        String theResourceName = WASMStringPool.getStringPool(aStringPoolResourceName);

        WASMLogger.INSTANCE.info("Received resource " + theResourceName + " for spritesheet " + aSpriteSheet+ " with external id " + aExternalResourceID);

        theSheet.registerResource(theResourceName, aExternalResourceID);
    }

    public WASMGameResourceLoader(String aSceneId) {
        sceneId = aSceneId;
    }

    @Override
    public Promise<GameResource, String> load(ResourceName aResourceName) {
        WASMLogger.INSTANCE.info("Loading Game Resource " +  aResourceName.get());
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
            }
        });
    }

    @Override
    public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {
        WASMLogger.INSTANCE.info("Loading sprite sheet " +  aResourceName.get());

        ResourceName theNewResourceName = new ResourceName(sceneId + aResourceName.name.replace('\\', '/'));
        final String thePath = theNewResourceName.name.replace('\\', '/');

        LOADINGSHEETS.put(thePath, new WASMLoadedSpriteSheet());

        Promise<LoadedSpriteSheet, String> theResult = new Promise<>();

        Promise<Long, Long> theShadow = new Promise<>();
        theShadow.thenContinue(aResult -> {
            WASMLogger.INSTANCE.info("Sprite sheet loaded as external ID " + aResult);
            theResult.resolve(LOADINGSHEETS.remove(thePath));
        });

        String theExternalPromise = UUID.randomUID();
        PROMISES.put(theExternalPromise, theShadow);

        loadSpriteSheet(theExternalPromise, thePath);

        return theResult;
    }

    @Override
    public void flush() {
    }
}