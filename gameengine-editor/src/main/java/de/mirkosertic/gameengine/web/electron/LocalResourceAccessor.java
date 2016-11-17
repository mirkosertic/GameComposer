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
package de.mirkosertic.gameengine.web.electron;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMLoadedSpriteSheet;
import de.mirkosertic.gameengine.teavm.pixi.Loader;
import de.mirkosertic.gameengine.teavm.pixi.LoaderCallchain;
import de.mirkosertic.gameengine.teavm.pixi.LoaderResource;
import de.mirkosertic.gameengine.teavm.pixi.SpritesheetJSONResource;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.web.html5.Blob;
import de.mirkosertic.gameengine.web.html5.File;
import de.mirkosertic.gameengine.web.ResourceAccessor;
import de.mirkosertic.gameengine.web.electron.fs.FS;

public class LocalResourceAccessor implements ResourceAccessor {

    private final FS fs;
    private final String localPath;

    public LocalResourceAccessor(FS aFS, String aLocalPath) {
        fs = aFS;
        localPath = aLocalPath;
    }

    @Override
    public Promise<File, String> persistFile(String aFileName, Blob aContent) {
        throw new IllegalStateException("Not implemented!");
    }

    @Override
    public TeaVMGameSceneLoader createSceneLoader(AbstractGameRuntimeFactory aRuntimeFactory) {
        return new TeaVMGameSceneLoader(aRuntimeFactory) {
            @Override
            public Promise<GameScene, String> loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {
                return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
                    String theFile = localPath + "/" + aSceneName + "/scene.json";
                    String theData = fs.readFileSync(theFile, "utf8");

                    GameScene theScene = parse(aGame, theData, aResourceLoader);
                    aResolver.resolve(theScene);
                });
            }
        };
    }

    @Override
    public TeaVMGameLoader createGameLoader() {
        return new TeaVMGameLoader() {
            @Override
            public Promise<Game, String> loadFromServer() {
                return new Promise<>((aResolver, aRejector) -> {
                    String theFile = localPath + "/game.json";
                    String theData = fs.readFileSync(theFile, "utf8");
                    Game theGame = parse(theData);
                    aResolver.resolve(theGame);
                });
            }
        };
    }

    @Override
    public TeaVMGameResourceLoader createResourceLoaderFor(String aSceneID) {
        return new TeaVMGameResourceLoader(aSceneID) {
            @Override
            public Promise<GameResource, String> load(ResourceName aResourceName) {
                return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
                    String theFile = localPath + "/" + aSceneID + aResourceName.name;
                    theFile = theFile.replace('\\', '/');
                    if (!theFile.startsWith("/")) {
                        theFile = "/" + theFile;
                    }

                    String theURL = "file://" + theFile;

                    ResourceName theNewResourceName = new ResourceName(theURL);
                    aResolver.resolve(convert(theNewResourceName, theNewResourceName));
                });
            }

            @Override
            public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {

                String theFile = localPath + "/" + aSceneID + aResourceName.name;
                theFile = theFile.replace('\\', '/');
                if (!theFile.startsWith("/")) {
                    theFile = "/" + theFile;
                }

                String theURL = "file://" + theFile;

                return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
                    ResourceName theNewResourceName = new ResourceName(theURL);

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
        };
    }
}