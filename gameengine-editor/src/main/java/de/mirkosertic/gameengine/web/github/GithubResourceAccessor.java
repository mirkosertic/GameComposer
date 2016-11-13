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
package de.mirkosertic.gameengine.web.github;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.*;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.web.*;
import org.teavm.jso.ajax.XMLHttpRequest;

public class GithubResourceAccessor implements ResourceAccessor {

    private final String baseURL;
    private final BlobLoader blobLoader;
    private final Filesystem fileSystem;

    public GithubResourceAccessor(String aBaseURL, Filesystem aFileSystem) {
        baseURL = aBaseURL;
        fileSystem = aFileSystem;
        blobLoader = new BlobLoader();
    }

    @Override
    public Promise<File, String> persistFile(String aFileName, Blob aContent) {
        return fileSystem.updateFile(aFileName, aContent);
    }

    @Override
    public TeaVMGameSceneLoader createSceneLoader(TeaVMGameSceneLoader.GameSceneLoadedListener aListener,
            AbstractGameRuntimeFactory aRuntimeFactory) {
        return new TeaVMGameSceneLoader(aListener, aRuntimeFactory) {
            @Override
            public void loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {

                String theFileName = "/" + aSceneName + "/scene.json";
                fileSystem.openFile(theFileName).thenContinue(aResult -> {
                    fileSystem.asDataURL(aResult, aValue -> {
                        XMLHttpRequest theRequest = XMLHttpRequest.create();
                        theRequest.overrideMimeType("text/plain");
                        theRequest.open("GET", aValue);
                        theRequest.onComplete(() -> listener.onGameSceneLoaded(parse(aGame, theRequest.getResponseText(), aResourceLoader)));
                        theRequest.send();
                    });
                }).catchError(aResult -> {
                    String theURL = baseURL + theFileName;
                    blobLoader.load(theURL, (aURL, aBlob) -> fileSystem.storeFile(theFileName, aBlob).thenContinue(aResult1 -> {
                        fileSystem.asDataURL(aResult1, aValue -> {
                            XMLHttpRequest theRequest = XMLHttpRequest.create();
                            theRequest.overrideMimeType("text/plain");
                            theRequest.open("GET", aValue);
                            theRequest.onComplete(() -> listener.onGameSceneLoaded(parse(aGame, theRequest.getResponseText(), aResourceLoader)));
                            theRequest.send();
                        });
                    }).catchError(aResult1 -> TeaVMLogger.error("Error writing file : " + aResult1)));
                });
            }
        };
    }

    @Override
    public TeaVMGameLoader createGameLoader(TeaVMGameLoader.GameLoadedListener aListener) {
        return new TeaVMGameLoader(aListener) {
            @Override
            public void loadFromServer() {
                String theFileName = "/game.json";
                fileSystem.openFile(theFileName).thenContinue(aResult -> {
                    fileSystem.asDataURL(aResult, aValue -> {
                        final XMLHttpRequest theRequest = XMLHttpRequest.create();
                        theRequest.overrideMimeType("text/plain");
                        theRequest.open("GET", aValue);
                        theRequest.onComplete(() -> listener.onGameLoaded(parse(theRequest.getResponseText())));
                        theRequest.send();
                    });
                }).catchError(aResult -> {
                    String theURL = baseURL + theFileName;
                    blobLoader.load(theURL, (aURL, aBlob) -> fileSystem.storeFile(theFileName, aBlob).thenContinue(aResult1 -> {
                        fileSystem.asDataURL(aResult1, aValue -> {
                            final XMLHttpRequest theRequest = XMLHttpRequest.create();
                            theRequest.overrideMimeType("text/plain");
                            theRequest.open("GET", aValue);
                            theRequest.onComplete(() -> listener.onGameLoaded(parse(theRequest.getResponseText())));
                            theRequest.send();
                        });
                    }).catchError(aResult1 -> TeaVMLogger.error("Error writing file : " + aResult1)));
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
                    String theFileName = "/" + aSceneID + aResourceName.get();

                    fileSystem.openFile(theFileName).thenContinue(aResult -> {
                        TeaVMLogger.info("Loaded " + aResult.getFilename() + " from cache");

                        fileSystem.asDataURL(aResult, aValue -> aResolver.resolve(convert(aResourceName, new ResourceName(aValue))));
                    }).catchError(aResult -> {
                        String theURL = baseURL + theFileName;

                        blobLoader.load(theURL, (aURL, aBlob) -> fileSystem.storeFile(theFileName, aBlob).thenContinue(aResult1 -> {
                            TeaVMLogger.info("Written and Loaded " + aResult1.getFilename() + " from cache");

                            fileSystem.asDataURL(aResult1, aValue -> aResolver.resolve(convert(aResourceName, new ResourceName(aValue))));
                        }).catchError(aResult1 -> TeaVMLogger.error("Error writing file : " + aResult1)));
                    });
                });
            }

            @Override
            public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {

                String theFileName = "/" + aSceneID + aResourceName.get();

                TeaVMLogger.info("Continuing spritesheet loading for " + theFileName);

                ResourceName theNewResourceName = new ResourceName(baseURL + theFileName);

                return new Promise<>((Promise.Executor) (aResolver, aRejector) -> new TeaVMLoadedSpriteSheet(theNewResourceName, aSpriteSheet -> aResolver.resolve(aSpriteSheet)));
            }
        };
    }
}