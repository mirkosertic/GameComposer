/*
 * Copyright 2019 Mirko Sertic
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
package de.mirkosertic.gameengine.bytecoder;

import de.mirkosertic.gameengine.bytecoder.pixi.Loader;
import de.mirkosertic.gameengine.bytecoder.pixi.LoaderResource;
import de.mirkosertic.gameengine.bytecoder.pixi.SpritesheetJSONResource;
import de.mirkosertic.gameengine.bytecoder.pixi.Texture;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class BytecoderGameResourceLoader implements GameResourceLoader {

    private final String sceneId;
    private final Map<ResourceName, BytecoderTextureResource> textureResources;


    public BytecoderGameResourceLoader(String sceneId) {
        this.sceneId = sceneId;
        this.textureResources = new HashMap<>();
    }

    @Override
    public Promise<GameResource, String> load(ResourceName aResourceName) {
        BytecoderLogger.INSTANCE.info("Trying to load game resouce " + aResourceName.name);
        ResourceName theNewResourceName = new ResourceName(sceneId + aResourceName.name.replace('\\', '/'));
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
                aResolver.resolve(convert(theNewResourceName, theNewResourceName));
            }
        });
    }

    @Override
    public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {
        BytecoderLogger.INSTANCE.info("Trying to load spritesheet");
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
                ResourceName theNewResourceName = new ResourceName(sceneId + aResourceName.name.replace('\\', '/'));

                Loader theLoader = Loader.create();
                final String thePath = theNewResourceName.name.replace('\\', '/');

                BytecoderLogger.INSTANCE.info("Trying to load sprite sheet " + thePath);

                theLoader.add(thePath);
                theLoader.load((aLoader, aResources) -> {
                    BytecoderLogger.INSTANCE.info("Got result from loader");
                    LoaderResource theLoadedJSON = Loader.resourceByName(aResources, thePath);
                    if (theLoadedJSON != null) {
                        BytecoderLogger.INSTANCE.info("Done loading");
                        aResolver.resolve(new BytecoderLoadedSpriteSheet((SpritesheetJSONResource) theLoadedJSON.getData()));
                    } else {
                        BytecoderLogger.INSTANCE.error("Failed to load!");
                    }
                });
            }
        });
    }

    protected GameResource convert(ResourceName aOriginalResourceName, ResourceName aResourceName) {
        BytecoderTextureResource theResource = textureResources.get(aResourceName);
        if (theResource == null) {
            Texture theTexture = Texture.createTextureFromImage(aResourceName.name);
            theResource = new BytecoderTextureResource(theTexture, aResourceName.name);
            textureResources.put(aResourceName, theResource);
        }

        return theResource;
    }

    @Override
    public void flush() {
        for (Map.Entry<ResourceName, BytecoderTextureResource> aEntry : textureResources.entrySet()) {
            aEntry.getValue().getTexture().destroy();
        }
        textureResources.clear();
    }
}
