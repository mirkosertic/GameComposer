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

import org.teavm.jso.ajax.XMLHttpRequest;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMLoadedSpriteSheet;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.web.Blob;
import de.mirkosertic.gameengine.web.BlobLoader;
import de.mirkosertic.gameengine.web.File;
import de.mirkosertic.gameengine.web.Filesystem;
import de.mirkosertic.gameengine.web.ResourceAccessor;

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
                    fileSystem.asDataURL(aResult).thenContinue(aDataURL -> {
                        XMLHttpRequest theRequest = XMLHttpRequest.create();
                        theRequest.overrideMimeType("text/plain");
                        theRequest.open("GET", aDataURL);
                        theRequest.onComplete(() -> listener.onGameSceneLoaded(parse(aGame, theRequest.getResponseText(), aResourceLoader)));
                        theRequest.send();
                    });
                }).catchError(aResult -> {
                    String theURL = baseURL + theFileName;
                    blobLoader.load(theURL).thenContinue(aBlob -> {
                        fileSystem.storeFile(theFileName, aBlob).thenContinue(aFile -> {
                            fileSystem.asDataURL(aFile).thenContinue(aDataURL -> {
                                XMLHttpRequest theRequest = XMLHttpRequest.create();
                                theRequest.overrideMimeType("text/plain");
                                theRequest.open("GET", aDataURL);
                                theRequest.onComplete(() -> listener.onGameSceneLoaded(parse(aGame, theRequest.getResponseText(), aResourceLoader)));
                                theRequest.send();
                            });
                        });
                    }).catchError(aErrorMessage -> TeaVMLogger.error("Error writing file : " + aErrorMessage));
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

                    fileSystem.asDataURL(aResult).thenContinue(aDataURL -> {
                        final XMLHttpRequest theRequest = XMLHttpRequest.create();
                        theRequest.overrideMimeType("text/plain");
                        theRequest.open("GET", aDataURL);
                        theRequest.onComplete(() -> listener.onGameLoaded(parse(theRequest.getResponseText())));
                        theRequest.send();
                    });

                }).catchError(aResult -> {
                    String theURL = baseURL + theFileName;

                    blobLoader.load(theURL).thenContinue(aBlob -> {
                        fileSystem.storeFile(theFileName, aBlob).thenContinue(aFile -> {
                            fileSystem.asDataURL(aFile).thenContinue(aDataURL -> {
                                final XMLHttpRequest theRequest = XMLHttpRequest.create();
                                theRequest.overrideMimeType("text/plain");
                                theRequest.open("GET", aDataURL);
                                theRequest.onComplete(() -> listener.onGameLoaded(parse(theRequest.getResponseText())));
                                theRequest.send();
                            });
                        });
                    }).catchError(aErrorMessage -> TeaVMLogger.error("Error writing file : " + aErrorMessage));
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

                        fileSystem.asDataURL(aResult).thenContinue(aResult1 -> {
                            aResolver.resolve(convert(aResourceName, new ResourceName(aResult1)));
                        });

                    }).catchError(aResult -> {
                        String theURL = baseURL + theFileName;

                        blobLoader.load(theURL).thenContinue(aResult1 -> {
                            fileSystem.storeFile(theFileName, aResult1).thenContinue(aFile -> {
                                fileSystem.asDataURL(aFile).thenContinue(aDataURL -> {
                                    aResolver.resolve(convert(aResourceName, new ResourceName(aDataURL)));
                                });
                            });
                        }).catchError(aErrorMessage -> TeaVMLogger.error("Error writing file : " + aErrorMessage));
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