package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameRuntimeFactory;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGenericPlayer;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.web.github.AuthenticationOptions;
import de.mirkosertic.gameengine.web.github.File;
import de.mirkosertic.gameengine.web.github.FileVisitor;
import de.mirkosertic.gameengine.web.github.Github;
import de.mirkosertic.gameengine.web.github.GithubEditorProject;
import de.mirkosertic.gameengine.web.github.Repository;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class WebUI {

    private static final Window window = Window.current();

    public static void main(String[] args) {

        //EditorProject theProject = new LocalEditorProject();
        EditorProject theProject = new GithubEditorProject("mirkosertic", "GameComposer", "/examples/platformer");

        if (window.getLocation().getPathName().endsWith("/index.html")) {

            Editor theEditor = new Editor();
            theEditor.boot(theProject);

        } else if (window.getLocation().getPathName().endsWith("/gittest.html")) {
            TeaVMLogger.info("Starting to work!");
            AuthenticationOptions theOptions = new AuthenticationOptions();
            Github theGithub = new Github(theOptions);
            Repository theRepo = theGithub.getRepo("mirkosertic", "GameComposer");
            theRepo.visitContent(new FileVisitor() {
                @Override
                public void visit(File aFile, int aStatusCode, String aETag) {
                    if (aFile.getType().equals(File.DIR_TYPE)) {
                        theRepo.visitContent(aFile, this, null);
                    } else{
                        TeaVMLogger.info("Got file " + aFile.getPath() + " with ETag " + aETag);
                    }
                }
            });
        } else {
            HTMLCanvasElement theCanvasElement = (HTMLCanvasElement) window.getDocument().getElementById("html5canvas");
            TeaVMGenericPlayer thePlayer = new TeaVMGenericPlayer() {
                @Override
                protected TeaVMGameSceneLoader createSceneLoader(TeaVMGameRuntimeFactory aRuntimeFactory) {

                    String thePreviewData = theProject.getPreviewDataAsJSON();
                    if (thePreviewData != null) {
                        return new TeaVMGameSceneLoader(createSceneLoaderListener(), aRuntimeFactory) {
                            @Override
                            public void loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {
                                String thePreviewData = window.getLocalStorage().getItem("previewscene");
                                listener.onGameSceneLoaded(parse(aGame, thePreviewData, aResourceLoader));
                            }
                        };
                    }
                    return theProject.createSceneLoader(createSceneLoaderListener(), aRuntimeFactory);
                }

                @Override
                protected TeaVMGameLoader createGameLoader(TeaVMGameLoader.GameLoadedListener aListener) {
                    return theProject.createGameLoader(aListener);
                }

                @Override
                protected TeaVMGameResourceLoader createResourceLoader(String aSceneID) {
                    return theProject.createResourceLoaderFor(aSceneID);
                }

                @Override
                protected void loadOtherSceneFromWithinGame(Game aGame, String aSceneID) {
                }
            };
            thePlayer.boot(theCanvasElement);
        }
    }
}