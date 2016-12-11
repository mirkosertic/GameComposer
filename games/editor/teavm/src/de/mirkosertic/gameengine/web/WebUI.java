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

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameRuntimeFactory;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGenericPlayer;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.web.electron.Electron;
import de.mirkosertic.gameengine.web.electron.LocalEditorProject;
import de.mirkosertic.gameengine.web.electron.LocalProjectDefinition;
import de.mirkosertic.gameengine.web.electron.Remote;
import de.mirkosertic.gameengine.web.electron.fs.FS;
import de.mirkosertic.gameengine.web.github.GithubAuthorizer;
import de.mirkosertic.gameengine.web.github.GithubEditorProject;
import de.mirkosertic.gameengine.web.github.GithubProjectDefinition;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class WebUI {

    private static final Window WINDOW = Window.current();

    public static void main(String[] args) {

        GithubAuthorizer theAuthorizer = new GithubAuthorizer();

        TeaVMLogger.info("Starting web editor");

        Router theRouter = new Router(WINDOW);

        theRouter.add("/index.html", aWindow -> {
            Welcome theWelcome = new Welcome(theRouter, theAuthorizer);
            theWelcome.run();
        });
        theRouter.add("/editor.html", aWindow -> {
            EditorProject theProject = getEditorProjectFromState(theRouter);

            theProject.initializeResourceAccessor().thenContinue(aResult -> {
                Editor theEditor = new Editor(theRouter, theAuthorizer);
                theEditor.boot(theProject, aResult);

            }).catchError((aResult, aOptionalRejectedException) -> TeaVMLogger.error("Error creating indexeddb filesystem!"));
        });

        theRouter.add("/preview.html", aWindow -> {
            EditorProject theProject = getEditorProjectFromState(theRouter);

            theProject.initializeResourceAccessor().thenContinue(aResult -> {

                HTMLCanvasElement theCanvasElement = (HTMLCanvasElement) aWindow.getDocument().getElementById("html5canvas");
                TeaVMGenericPlayer thePlayer = new TeaVMGenericPlayer() {
                    @Override
                    protected TeaVMGameSceneLoader createSceneLoader(TeaVMGameRuntimeFactory aRuntimeFactory) {

                        String thePreviewData = theProject.getPreviewDataAsJSON();
                        if (thePreviewData != null) {
                            return new TeaVMGameSceneLoader(aRuntimeFactory) {
                                @Override
                                public Promise<GameScene, String> loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {
                                    return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
                                        String thePreviewData1 = aWindow.getLocalStorage().getItem("previewscene");
                                        aResolver.resolve(parse(aGame, thePreviewData1, aResourceLoader));
                                    });
                                }
                            };
                        }
                        return aResult.createSceneLoader(aRuntimeFactory);
                    }

                    @Override
                    protected TeaVMGameLoader createGameLoader() {
                        return aResult.createGameLoader();
                    }

                    @Override
                    protected TeaVMGameResourceLoader createResourceLoader(String aSceneID) {
                        return aResult.createResourceLoaderFor(aSceneID);
                    }

                    @Override
                    protected void loadOtherSceneFromWithinGame(Game aGame, String aSceneID) {
                    }
                };
                thePlayer.boot(theCanvasElement);

            }).catchError((aResult, aOptionalRejectedException) -> TeaVMLogger.error("Error creating indexeddb filesystem!"));
        });
        theRouter.widhDefaultPath("/index.html");

        theRouter.handleNavigation();
    }

    private static EditorProject getEditorProjectFromState(Router aRouter) {
        ProjectDefinition theDefinition = aRouter.getCurrentState();
        switch (theDefinition.getType()) {
            case GithubProjectDefinition.TYPE:

                GithubProjectDefinition theGithubDefinition = (GithubProjectDefinition) theDefinition;

                TeaVMLogger.info("Loading Github Project " + theGithubDefinition.getUser() + "@" + theGithubDefinition.getRepository() + ":" + theGithubDefinition.getRelativePath());

                return new GithubEditorProject(theGithubDefinition);
            case LocalProjectDefinition.TYPE:
                LocalProjectDefinition theLocalDefintion = (LocalProjectDefinition) theDefinition;

                TeaVMLogger.info("Loading Local Electron Project " + theLocalDefintion.getPath());

                Remote theRemote = Electron.require().getRemote();
                FS theFilesystem = theRemote.require("fs");
                return new LocalEditorProject(theFilesystem, theLocalDefintion);
        }
        throw new IllegalArgumentException("not supported state");
    }
}