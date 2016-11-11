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
package de.mirkosertic.gameengine.web;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameRuntimeFactory;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGenericPlayer;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.web.github.GithubEditorProject;

public class WebUI {

    private static final Window WINDOW = Window.current();

    private static EditorProject getDefaultProject() {
        /*        if (Electron.available()) {

            Remote theRemote = Electron.require().getRemote();
            FS theFilesystem = theRemote.require("fs");

            return new LocalEditorProject(theFilesystem, "/home/sertic/Development/Projects/GameComposer/examples/platformer");
        }*/

        return new GithubEditorProject("mirkosertic", "GameComposer", "/examples/platformer");
    }

    public static void main(String[] args) {

        TeaVMLogger.info("Starting web editor");

        EditorProject theProject = getDefaultProject();

        theProject.initializeLoader(new EditorProject.Callback() {
            @Override
            public void onError(EditorProject aProject) {
                TeaVMLogger.error("Error creating indexeddb filesystem!");
            }

            @Override
            public void onSuccess(EditorProject aProject, ResourceAccessor aResourceLoaderFactory) {
                TeaVMLogger.info("ResourceLoader created");
                initializeWithResourceLoaderFactory(aResourceLoaderFactory, aProject);
            }
        });
    }

    private static void initializeWithResourceLoaderFactory(ResourceAccessor aResourceLoaderFactory, EditorProject aProject) {
        if (WINDOW.getLocation().getPathName().endsWith("/index.html")) {

            TeaVMLogger.info("Starting editor");

            Editor theEditor = new Editor();
            theEditor.boot(aProject, aResourceLoaderFactory);

        } else {

            TeaVMLogger.info("Starting preview");

            HTMLCanvasElement theCanvasElement = (HTMLCanvasElement) WINDOW.getDocument().getElementById("html5canvas");
            TeaVMGenericPlayer thePlayer = new TeaVMGenericPlayer() {
                @Override
                protected TeaVMGameSceneLoader createSceneLoader(TeaVMGameRuntimeFactory aRuntimeFactory) {

                    String thePreviewData = aProject.getPreviewDataAsJSON();
                    if (thePreviewData != null) {
                        return new TeaVMGameSceneLoader(createSceneLoaderListener(), aRuntimeFactory) {
                            @Override
                            public void loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {
                                String thePreviewData = WINDOW.getLocalStorage().getItem("previewscene");
                                listener.onGameSceneLoaded(parse(aGame, thePreviewData, aResourceLoader));
                            }
                        };
                    }
                    return aResourceLoaderFactory.createSceneLoader(createSceneLoaderListener(), aRuntimeFactory);
                }

                @Override
                protected TeaVMGameLoader createGameLoader(TeaVMGameLoader.GameLoadedListener aListener) {
                    return aResourceLoaderFactory.createGameLoader(aListener);
                }

                @Override
                protected TeaVMGameResourceLoader createResourceLoader(String aSceneID) {
                    return aResourceLoaderFactory.createResourceLoaderFor(aSceneID);
                }

                @Override
                protected void loadOtherSceneFromWithinGame(Game aGame, String aSceneID) {
                }
            };
            thePlayer.boot(theCanvasElement);
        }
    }
}