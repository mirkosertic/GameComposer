package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.teavm.*;
import de.mirkosertic.gameengine.web.github.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class WebUI {

    private static final Window window = Window.current();

    public static void main(String[] args) {
        if (window.getLocation().getPathName().endsWith("/index.html")) {
            Editor theEditor = new Editor();
            theEditor.boot();
        } else if (window.getLocation().getPathName().endsWith("/gittest.html")) {
            TeaVMLogger.info("Starting to work!");
            AuthenticationOptions theOptions = AuthenticationOptions.create();
            Github theGithub = Github.create(theOptions);
            Repository theRepo = theGithub.getRepo("mirkosertic", "FXDesktopSearch");
            theRepo.getCommit("master", new Callback<Commit>() {
                @Override
                public void handle(Boolean aError, Commit aResult) {
                    TeaVMLogger.info("Got something from commit");
                    TeaVMLogger.info("Last Commit of " + theRepo.getName() + " -> " + aResult.getSha());
                }
            });
        } else {
            HTMLCanvasElement theCanvasElement = (HTMLCanvasElement) window.getDocument().getElementById("html5canvas");
            TeaVMGenericPlayer thePlayer = new TeaVMGenericPlayer() {
                @Override
                protected TeaVMGameSceneLoader createSceneLoader(TeaVMGameRuntimeFactory aRuntimeFactory) {
                    return new TeaVMGameSceneLoader(createSceneLoaderListener(), aRuntimeFactory) {
                        @Override
                        public void loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {
                            String thePreviewData = window.getLocalStorage().getItem("previewscene");
                            if (thePreviewData == null) {
                                super.loadFromServer(aGame, aSceneName, aResourceLoader);
                                return;
                            }
                            listener.onGameSceneLoaded(parse(aGame, thePreviewData, aResourceLoader));
                        }
                    };
                }

                @Override
                protected void loadOtherSceneFromWithinGame(Game aGame, String aSceneID) {
                }
            };
            thePlayer.boot(theCanvasElement);
        }
    }
}