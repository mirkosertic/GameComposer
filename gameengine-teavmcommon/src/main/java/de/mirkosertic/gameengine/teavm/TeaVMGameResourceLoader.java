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
package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.pixi.Loader;
import de.mirkosertic.gameengine.teavm.pixi.LoaderCallchain;
import de.mirkosertic.gameengine.teavm.pixi.LoaderResource;
import de.mirkosertic.gameengine.teavm.pixi.SpritesheetJSONResource;
import de.mirkosertic.gameengine.teavm.pixi.Texture;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class TeaVMGameResourceLoader implements GameResourceLoader {

    private final String sceneId;
    private final Map<ResourceName, TeaVMTextureResource> textureResources;

    public TeaVMGameResourceLoader(String aSceneID) {
        sceneId = aSceneID;
        textureResources = new HashMap<>();
    }

    @Override
    public Promise<GameResource, String> load(ResourceName aResourceName) {
        ResourceName theNewResourceName = new ResourceName(sceneId + aResourceName.name.replace('\\', '/'));
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> aResolver.resolve(convert(theNewResourceName, theNewResourceName)));
    }

    @Override
    public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {

            ResourceName theNewResourceName = new ResourceName(sceneId + aResourceName.name.replace('\\', '/'));

            Loader theLoader = Loader.create();
            final String thePath = theNewResourceName.name.replace('\\', '/');
            theLoader.add(thePath);
            theLoader.pre((aResource, aChain) -> {
                // This does nothing
                LoaderCallchain.next(aChain);
            });
            theLoader.load((aLoader, aResources) -> {
                LoaderResource theLoadedJSON = aResources.get(thePath);
                if (theLoadedJSON != null) {
                    aResolver.resolve(new TeaVMLoadedSpriteSheet((SpritesheetJSONResource) theLoadedJSON.getData()));
                }
            });
        });
    }

    protected GameResource convert(ResourceName aOriginalResourceName, ResourceName aResourceName) {
        if (aOriginalResourceName.name.endsWith(".wav")) {
            return new TeaVMSoundResource(aResourceName.name);
        }

        TeaVMTextureResource theResource = textureResources.get(aResourceName);
        if (theResource == null) {
            Texture theTexture = Texture.createTextureFromImage(aResourceName.name);
            theResource = new TeaVMTextureResource(theTexture, aResourceName.name);
            textureResources.put(aResourceName, theResource);
        }

        return theResource;
    }

    @Override
    public void flush() {

        for (Map.Entry<ResourceName, TeaVMTextureResource> aEntry : textureResources.entrySet()) {
            aEntry.getValue().getTexture().destroy();
        }
        textureResources.clear();
    }
}