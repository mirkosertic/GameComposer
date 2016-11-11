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

import java.io.IOException;

import org.teavm.jso.ajax.XMLHttpRequest;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
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
    public void persistFile(String aFileName, Blob aContent, CompleteCallback aCallback) {
        fileSystem.updateFile(aFileName, aContent, new Filesystem.FileProcessor() {
            @Override
            public void process(File aFile) {
                aCallback.fileWritten();
            }

            @Override
            public void doesNotExist(String aFileName) {
                aCallback.error("Error writing file " + aFileName);
            }
        });
    }

    @Override
    public TeaVMGameSceneLoader createSceneLoader(TeaVMGameSceneLoader.GameSceneLoadedListener aListener,
            AbstractGameRuntimeFactory aRuntimeFactory) {
        return new TeaVMGameSceneLoader(aListener, aRuntimeFactory) {
            @Override
            public void loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {

                String theFileName = "/" + aSceneName + "/scene.json";
                fileSystem.openFile(theFileName, new Filesystem.FileProcessor() {

                    @Override
                    public void process(File aFile) {
                        fileSystem.asDataURL(aFile, aValue -> {
                            XMLHttpRequest theRequest = XMLHttpRequest.create();
                            theRequest.overrideMimeType("text/plain");
                            theRequest.open("GET", aValue);
                            theRequest.onComplete(() -> listener.onGameSceneLoaded(parse(aGame, theRequest.getResponseText(), aResourceLoader)));
                            theRequest.send();
                        });
                    }

                    @Override
                    public void doesNotExist(String aFileName) {

                        String theURL = baseURL + theFileName;
                        blobLoader.load(theURL, (aURL, aBlob) -> fileSystem.storeFile(theFileName, aBlob, new Filesystem.FileProcessor() {
                            @Override
                            public void process(File aFile) {
                                fileSystem.asDataURL(aFile, aValue -> {
                                    XMLHttpRequest theRequest = XMLHttpRequest.create();
                                    theRequest.overrideMimeType("text/plain");
                                    theRequest.open("GET", aValue);
                                    theRequest.onComplete(() -> listener.onGameSceneLoaded(parse(aGame, theRequest.getResponseText(), aResourceLoader)));
                                    theRequest.send();
                                });
                            }

                            @Override
                            public void doesNotExist(String aFileName1) {
                                TeaVMLogger.error("Error writing file " + aFileName1);
                            }
                        }));
                    }
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
                fileSystem.openFile(theFileName, new Filesystem.FileProcessor() {

                    @Override
                    public void process(File aFile) {
                        fileSystem.asDataURL(aFile, aValue -> {
                            final XMLHttpRequest theRequest = XMLHttpRequest.create();
                            theRequest.overrideMimeType("text/plain");
                            theRequest.open("GET", aValue);
                            theRequest.onComplete(() -> listener.onGameLoaded(parse(theRequest.getResponseText())));
                            theRequest.send();
                        });
                    }

                    @Override
                    public void doesNotExist(String aFileName) {

                        String theURL = baseURL + theFileName;

                        blobLoader.load(theURL, (aURL, aBlob) -> fileSystem.storeFile(theFileName, aBlob, new Filesystem.FileProcessor() {
                            @Override
                            public void process(File aFile) {
                                fileSystem.asDataURL(aFile, aValue -> {
                                    final XMLHttpRequest theRequest = XMLHttpRequest.create();
                                    theRequest.overrideMimeType("text/plain");
                                    theRequest.open("GET", aValue);
                                    theRequest.onComplete(() -> listener.onGameLoaded(parse(theRequest.getResponseText())));
                                    theRequest.send();
                                });
                            }

                            @Override
                            public void doesNotExist(String aFileName1) {
                                TeaVMLogger.error("Error writing file " + aFileName1);
                            }
                        }));
                    }
                });
            }
        };
    }

    @Override
    public TeaVMGameResourceLoader createResourceLoaderFor(String aSceneID) {
        return new TeaVMGameResourceLoader(aSceneID) {

            @Override
            public void load(ResourceName aResourceName, Listener aListener) throws IOException {

                String theFileName = "/" + aSceneID + aResourceName.get();

                fileSystem.openFile(theFileName, new Filesystem.FileProcessor() {

                    @Override
                    public void process(File aFile) {
                        // We are done here
                        TeaVMLogger.info("Loaded " + aFile.getFilename() + " from cache");

                        fileSystem.asDataURL(aFile, aValue -> aListener.handle(convert(aResourceName, new ResourceName(aValue))));
                    }

                    @Override
                    public void doesNotExist(String aFileName) {

                        String theURL = baseURL + theFileName;

                        blobLoader.load(theURL, (aURL, aBlob) -> fileSystem.storeFile(theFileName, aBlob, new Filesystem.FileProcessor() {
                            @Override
                            public void process(File aFile) {
                                TeaVMLogger.info("Written and Loaded " + aFile.getFilename() + " from cache");

                                fileSystem.asDataURL(aFile, aValue -> aListener.handle(convert(aResourceName, new ResourceName(aValue))));
                            }

                            @Override
                            public void doesNotExist(String aFileName1) {
                                TeaVMLogger.error("Error writing file " + aFileName1);
                            }
                        }));
                    }
                });
            }

            @Override
            public void loadSpriteSheet(ResourceName aResourceName, SpritesheetListener aListener) {

                String theFileName = "/" + aSceneID + aResourceName.get();

                TeaVMLogger.info("Continuing spritesheet loading for " + theFileName);

                ResourceName theNewResourceName = new ResourceName(baseURL + theFileName);

                new TeaVMLoadedSpriteSheet(theNewResourceName, aSpriteSheet -> aListener.handle(aSpriteSheet));
            }
        };
    }
}