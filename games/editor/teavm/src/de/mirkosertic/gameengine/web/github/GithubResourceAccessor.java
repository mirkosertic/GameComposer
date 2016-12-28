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
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMLoadedSpriteSheet;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.teavm.pixi.Loader;
import de.mirkosertic.gameengine.teavm.pixi.LoaderCallchain;
import de.mirkosertic.gameengine.teavm.pixi.LoaderResource;
import de.mirkosertic.gameengine.teavm.pixi.SpritesheetJSONResource;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.web.AuthorizationState;
import de.mirkosertic.gameengine.web.BlobLoader;
import de.mirkosertic.gameengine.web.EditorProject;
import de.mirkosertic.gameengine.web.Filesystem;
import de.mirkosertic.gameengine.web.ResourceAccessor;
import de.mirkosertic.gameengine.web.html5.Blob;
import de.mirkosertic.gameengine.web.html5.File;
import de.mirkosertic.gameengine.web.html5.FileReader;
import de.mirkosertic.gameengine.web.indexeddb.IndexedDBFilesystem;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.jso.json.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GithubResourceAccessor implements ResourceAccessor {

    private final String baseURL;
    private final BlobLoader blobLoader;
    private final Filesystem fileSystem;
    private final GithubProjectDefinition projectDefinition;

    public GithubResourceAccessor(GithubProjectDefinition aDefinition , IndexedDBFilesystem aFileSystem) {
        projectDefinition = aDefinition;
        baseURL = "https://raw.githubusercontent.com/" + projectDefinition.getUser() + "/" + projectDefinition.getRepository() + "/master" + projectDefinition.getRelativePath();
        fileSystem = aFileSystem;
        blobLoader = new BlobLoader();
    }

    @Override
    public Promise<File, String> persistFile(String aFileName, Blob aContent) {
        return fileSystem.updateFile(aFileName, aContent);
    }

    @Override
    public TeaVMGameSceneLoader createSceneLoader(AbstractGameRuntimeFactory aRuntimeFactory) {
        return new TeaVMGameSceneLoader(aRuntimeFactory) {
            @Override
            public Promise<GameScene, String> loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {

                return new Promise<>((aResolver, aRejector) -> {
                    String theFileName = "/" + aSceneName + "/scene.json";
                    fileSystem.openFile(theFileName).thenContinue(aResult -> {
                        fileSystem.asDataURL(aResult).thenContinue(aDataURL -> {
                            blobLoader.loadAsText(aDataURL).thenContinue(aResult1 -> {
                                aResolver.resolve(parse(aGame, aResult1, aResourceLoader));
                            });
                        });
                    }).catchError((aResult, aOptionalRejectedException) -> {
                        String theURL = baseURL + theFileName;
                        blobLoader.load(theURL).thenContinue(aBlob -> {
                            fileSystem.storeFile(theFileName, aBlob).thenContinue(aFile -> {
                                fileSystem.asDataURL(aFile).thenContinue(aDataURL -> {
                                    blobLoader.loadAsText(aDataURL).thenContinue(aResult1 -> {
                                        aResolver.resolve(parse(aGame, aResult1, aResourceLoader));
                                    });
                                });
                            });
                        }).catchError((aErrorMessage, aOptionalRejectedException1) -> TeaVMLogger.error("Error writing file : " + aErrorMessage));
                    });
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

                    String theFileName = "/game.json";
                    fileSystem.openFile(theFileName).thenContinue(aResult -> {

                        fileSystem.asDataURL(aResult).thenContinue(aDataURL -> {
                            blobLoader.loadAsText(aDataURL).thenContinue(aResult1 -> {
                                aResolver.resolve(parse(aResult1));
                            });
                        });

                    }).catchError((aResult, aOptionalRejectedException) -> {
                        String theURL = baseURL + theFileName;

                        blobLoader.load(theURL).thenContinue(aBlob -> {
                            fileSystem.storeFile(theFileName, aBlob).thenContinue(aFile -> {
                                fileSystem.asDataURL(aFile).thenContinue(aDataURL -> {
                                    blobLoader.loadAsText(aDataURL).thenContinue(aResult1 -> {
                                        aResolver.resolve(parse(aResult1));
                                    });
                                });
                            });
                        }).catchError((aErrorMessage, aOptionalRejectedException1) -> TeaVMLogger.error("Error writing file : " + aErrorMessage));
                    });
                });
            }
        };
    }

    private void updateResource(LoaderResource aResource, String aDataUri, String aOriginalFileName, LoaderCallchain aChain) {
        TeaVMLogger.info("Loading Data URI for " + aOriginalFileName);
        if (aOriginalFileName.endsWith(".json")) {
            blobLoader.loadAsText(aDataUri).thenContinue(aResult -> {
                aResource.setData(JSON.parse(aResult));
                aResource.setIsJson(true);
                LoaderCallchain.next(aChain);
            });
        } else {
            // Data ist bei Bildern ein Image Element
            HTMLImageElement theImageElement = (HTMLImageElement) Window.current().getDocument().createElement("img");
            theImageElement.setSrc(aDataUri);
            aResource.setData(theImageElement);
            LoaderCallchain.next(aChain);
        }
    }

    @Override
    public TeaVMGameResourceLoader createResourceLoaderFor(String aSceneID) {
        return new TeaVMGameResourceLoader(aSceneID) {

            @Override
            public Promise<GameResource, String> load(ResourceName aResourceName) {
                return new Promise<>((aResolver, aRejector) -> {
                    String theFileName = "/" + aSceneID + aResourceName.get();

                    fileSystem.openFile(theFileName).thenContinue(aResult -> {
                        fileSystem.asDataURL(aResult).thenContinue(aResult1 -> {
                            aResolver.resolve(convert(aResourceName, new ResourceName(aResult1)));
                        });

                    }).catchError((aResult, aOptionalRejectedException) -> {
                        String theURL = baseURL + theFileName;

                        blobLoader.load(theURL).thenContinue(aResult1 -> {
                            fileSystem.storeFile(theFileName, aResult1).thenContinue(aFile -> {
                                fileSystem.asDataURL(aFile).thenContinue(aDataURL -> {
                                    aResolver.resolve(convert(aResourceName, new ResourceName(aDataURL)));
                                });
                            });
                        }).catchError((aErrorMessage, aOptionalRejectedException1) -> TeaVMLogger.error("Error writing file : " + aErrorMessage));
                    });
                });
            }

            @Override
            public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {

                String theFileName = "/" + aSceneID + aResourceName.get();

                TeaVMLogger.info("Continuing spritesheet loading for " + theFileName);

                Loader theLoader = Loader.create();
                theLoader.pre((aResource, aChain) -> {

                    String theResourceFilename = aResource.getUrl().substring(baseURL.length());

                    TeaVMLogger.info("Loading resource with caching loader : " + theResourceFilename);

                    // Implement cache interaction here to retrieve loaded pngs etc.
                    fileSystem.openFile(theResourceFilename).thenContinue(aStoresFile -> {
                        fileSystem.asDataURL(aStoresFile).thenContinue(aDataURI -> {
                            updateResource(aResource, aDataURI, theResourceFilename, aChain);
                        });
                    }).catchError((aFileName, aOptionalException) -> {
                        // Not cached, so needs to be loaded into cache

                        TeaVMLogger.info("Resource not cached " + aResource.getUrl());

                        blobLoader.load(aResource.getUrl()).thenContinue(aLoadedBlob -> {
                            fileSystem.storeFile(theResourceFilename, aLoadedBlob).thenContinue(
                                    aStoredFile -> {
                                        fileSystem.asDataURL(aStoredFile).thenContinue(
                                                aDataURI -> {
                                                    updateResource(aResource, aDataURI, theResourceFilename, aChain);
                                                });
                                    });
                        });
                    });

                    aResource.on("complete", () -> {
                        TeaVMLogger.info("Resource loaded " + theResourceFilename);
                    });
                });

                return new Promise<>((aResolver, aRejector) -> {

                    ResourceName theNewResourceName = new ResourceName(baseURL + theFileName);
                    final String thePath = theNewResourceName.name.replace('\\', '/');
                    theLoader.add(thePath);
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

    public Promise<GithubHead, String> getLatestHead(AuthorizationState aAuthorizationState) {
        return new Promise<>((aResolver, aRejector) -> {
            XMLHttpRequest theRequest = XMLHttpRequest.create();

            // https://api.github.com/repos/mirkosertic/GameComposer/git/refs/heads/master
            theRequest.open("GET", "https://api.github.com/repos/" + projectDefinition.getUser() + "/" + projectDefinition.getRepository()+"/git/refs/heads/master");
            theRequest.setRequestHeader("Authorization", "token " + aAuthorizationState.getToken());
            theRequest.overrideMimeType("text/plain");
            theRequest.onComplete(() -> {
                GithubHead theHead = (GithubHead) JSON.parse(theRequest.getResponseText());
                aResolver.resolve(theHead);
            });
            theRequest.send();
        });
    }

    public Promise<GithubHead, String> updateHead(AuthorizationState aAuthorizationState, String aCommitSHA) {
        GithubNewHead theNewHead = GithubNewHead.create();
        theNewHead.setSha(aCommitSHA);

        return new Promise<>((aResolver, aRejector) -> {
            XMLHttpRequest theRequest = XMLHttpRequest.create();

            // https://api.github.com/repos/mirkosertic/GameComposer/git/refs/heads/master
            theRequest.open("PATCH", "https://api.github.com/repos/" + projectDefinition.getUser() + "/" + projectDefinition.getRepository()+"/git/refs/heads/master");
            theRequest.setRequestHeader("Authorization", "token " + aAuthorizationState.getToken());
            theRequest.overrideMimeType("text/plain");
            theRequest.onComplete(() -> {
                GithubHead theHead = (GithubHead) JSON.parse(theRequest.getResponseText());
                aResolver.resolve(theHead);
            });
            theRequest.send(JSON.stringify(theNewHead));
        });
    }

    public Promise<GithubCommit, String> getCommit(AuthorizationState aAuthorizationState, String aCommitSHA) {
        return new Promise<>((aResolver, aRejector) -> {
            XMLHttpRequest theRequest = XMLHttpRequest.create();

            // https://api.github.com/repos/mirkosertic/GameComposer/git/commits/36a1e4239ce45c9558505dd7e5de774d798fd5fd
            theRequest.open("GET", "https://api.github.com/repos/" + projectDefinition.getUser() + "/" + projectDefinition.getRepository()+"/git/commits/" + aCommitSHA);
            theRequest.setRequestHeader("Authorization", "token " + aAuthorizationState.getToken());
            theRequest.overrideMimeType("text/plain");
            theRequest.onComplete(() -> {
                GithubCommit theCommit = (GithubCommit) JSON.parse(theRequest.getResponseText());
                aResolver.resolve(theCommit);
            });
            theRequest.send();
        });
    }

    public Promise<GithubTree, String> getTree(AuthorizationState aAuthorizationState, String aTreeSHA) {
        return new Promise<>((aResolver, aRejector) -> {
            XMLHttpRequest theRequest = XMLHttpRequest.create();

            // https://api.github.com/repos/mirkosertic/GameComposer/git/trees/a8ce3c2da3f0d1fc2a6c89af76c4de341fd55d7e?recursive=1
            theRequest.open("GET", "https://api.github.com/repos/" + projectDefinition.getUser() + "/" + projectDefinition.getRepository()+"/git/trees/" + aTreeSHA + "?recursive=1");
            theRequest.setRequestHeader("Authorization", "token " + aAuthorizationState.getToken());
            theRequest.overrideMimeType("text/plain");
            theRequest.onComplete(() -> {
                GithubTree theTree = (GithubTree) JSON.parse(theRequest.getResponseText());
                aResolver.resolve(theTree);
            });
            theRequest.send();
        });
    }

    public Promise<GithubTree, String> createTree(AuthorizationState aAuthorizationState, GithubTree aTree) {
        return new Promise<>((aResolver, aRejector) -> {
            XMLHttpRequest theRequest = XMLHttpRequest.create();

            theRequest.open("POST", "https://api.github.com/repos/" + projectDefinition.getUser() + "/" + projectDefinition.getRepository()+"/git/trees");
            theRequest.setRequestHeader("Authorization", "token " + aAuthorizationState.getToken());
            theRequest.overrideMimeType("text/plain");
            theRequest.onComplete(() -> {
                GithubTree theTree = (GithubTree) JSON.parse(theRequest.getResponseText());
                aResolver.resolve(theTree);
            });
            theRequest.send(JSON.stringify(aTree));
        });
    }

    private GithubTree copyTreeAndFillData(GithubTree aTree, Map<String, Promise<GithubBlob, String>> aNewBase64Data) {
        GithubTree theCopy = GithubTree.create();
        theCopy.setBase_tree(aTree.getSha());

        // Currently only adding and updating files is supported
        JSArray<GithubTree.TreeItem> theItems = JSArray.create();
        for (Map.Entry<String, Promise<GithubBlob, String>> theEntry : aNewBase64Data.entrySet()) {

            GithubBlob theBlob = theEntry.getValue().getResolvedValue();

            String theDirectoryInRepository = projectDefinition.getRelativePath();
            if (!theDirectoryInRepository.endsWith("/")) {
                theDirectoryInRepository = theDirectoryInRepository + "/";
            }
            // Github does not accept a leading slash
            if (theDirectoryInRepository.startsWith("/")) {
                theDirectoryInRepository = theDirectoryInRepository.substring(1);
            }

            // Filenames always have a slash at the beginning
            String theFileNameWithoutPrefix = theEntry.getKey().substring(1);

            String thePathInRepository = theDirectoryInRepository + theFileNameWithoutPrefix;
            TeaVMLogger.info("Updating file " + thePathInRepository + " with SHA " + theBlob.getSha());

            GithubTree.TreeItem theItem = GithubTree.TreeItem.create();
            theItem.setPath(thePathInRepository);
            theItem.setMode("100644");
            theItem.setType("blob");
            theItem.setSha(theBlob.getSha());
            theItems.push(theItem);
        }

        theCopy.setTree(theItems);
        return theCopy;
    }

    public Promise<GithubBlob, JSString> createBlob(AuthorizationState aAuthorizationState, JSString aBase64Data) {
        return new Promise<>((aResolver, aRejector) -> {
            XMLHttpRequest theRequest = XMLHttpRequest.create();

            theRequest.open("POST", "https://api.github.com/repos/" + projectDefinition.getUser() + "/" + projectDefinition.getRepository()+"/git/blobs");
            theRequest.setRequestHeader("Authorization", "token " + aAuthorizationState.getToken());
            theRequest.overrideMimeType("text/plain");
            theRequest.onComplete(() -> {
                GithubBlob theBlob = (GithubBlob) JSON.parse(theRequest.getResponseText());
                aResolver.resolve(theBlob);
            });

            GithubNewBlob theNewBlob = GithubNewBlob.create();
            theNewBlob.setEncoding("base64");
            theNewBlob.setContent(aBase64Data);
            theRequest.send(JSON.stringify(theNewBlob));
        });
    }

    public Promise<GithubCommit, String> commitTree(AuthorizationState aAuthorizationState, String aMessage, String aTreeSHA, String[] aParentCommits) {
        GithubNewCommit theCommit = GithubNewCommit.create();
        theCommit.setMessage(aMessage);
        theCommit.setTree(aTreeSHA);
        theCommit.setParents(aParentCommits);

        return new Promise<>((aResolver, aRejector) -> {
            XMLHttpRequest theRequest = XMLHttpRequest.create();

            theRequest.open("POST", "https://api.github.com/repos/" + projectDefinition.getUser() + "/" + projectDefinition.getRepository()+"/git/commits");
            theRequest.setRequestHeader("Authorization", "token " + aAuthorizationState.getToken());
            theRequest.overrideMimeType("text/plain");
            theRequest.onComplete(() -> {
                GithubTree theTree = (GithubTree) JSON.parse(theRequest.getResponseText());
                aResolver.resolve(theTree);
            });
            theRequest.send(JSON.stringify(theCommit));
        });
    }

    public Promise<GithubCommit, String> publish(AuthorizationState aAuthorizationState, String aCommitMessage) {
        return new Promise<>((aFinalResolver, aFinalRejector) -> getLatestHead(aAuthorizationState).thenContinue(aResult -> {
            String theHeadCommit = aResult.getObject().getSha();

            TeaVMLogger.info("Latest HEAD is " + theHeadCommit);

            // We need the commit
            getCommit(aAuthorizationState, theHeadCommit).thenContinue(aGithubHead -> {
                String theTreeSHA1 = aGithubHead.getTree().getSha();
                TeaVMLogger.info("Tree SHA is " + theTreeSHA1);

                getTree(aAuthorizationState, theTreeSHA1).thenContinue(aGithubTree -> {
                    TeaVMLogger.info("Found " + aGithubTree.getTree().getLength() + " files");

                    // Now we need to iterate over our files
                    fileSystem.listChangedFiles().thenContinue(aChangedFiles -> {
                        Map<String, Promise<GithubBlob, String>> theBase64Data = new HashMap<>();
                        for (File theFile : aChangedFiles) {
                            if (!EditorProject.DEFINITION_FILENAME.equals(theFile.getFilename())) {
                                theBase64Data.put(theFile.getFilename(), new Promise<>((aResolver, aRejector) -> {
                                    FileReader theReader = FileReader.create();
                                    theReader.setOnload(() -> {
                                        JSString theURI = JSString.valueOf(theReader.getResult());
                                        JSString theBase64 = theURI.substr(theURI.indexOf(JSString.valueOf(",")) + 1);

                                        // Upload to GitHub as Blob and store SHA
                                        createBlob(aAuthorizationState, theBase64).thenContinue(aLoadedBlob -> {
                                            TeaVMLogger.info("File " + theFile.getFilename() + " uploaded as blob");
                                            aResolver.resolve(aLoadedBlob);
                                        });
                                    });
                                    theReader.readAsDataURL(theFile.getContent());
                                }));
                                TeaVMLogger.info(theFile.getFilename() + " was changed");
                            }
                        }
                        Promise.all(new ArrayList<Promise>(theBase64Data.values())).thenContinue(aResult1 -> {
                            TeaVMLogger.info("All files loaded as base64");
                            GithubTree theTree = copyTreeAndFillData(aGithubTree, theBase64Data);
                            createTree(aAuthorizationState, theTree).thenContinue(aNewTree -> {
                                TeaVMLogger.info("New Tree created with hash  : " + aNewTree.getSha());
                                commitTree(aAuthorizationState, aCommitMessage, aNewTree.getSha(), new String[] {aGithubHead.getSha()}).thenContinue(aNewCommit -> {
                                    TeaVMLogger.info("New Commit created with hash  : " + aNewCommit.getSha());
                                    updateHead(aAuthorizationState, aNewCommit.getSha()).thenContinue(aResult11 -> {
                                        TeaVMLogger.info("HEAD updated to new commit");
                                        aFinalResolver.resolve(aNewCommit);
                                    });
                                });
                            });
                        });
                    });
                });
            });
        }));
    }
}