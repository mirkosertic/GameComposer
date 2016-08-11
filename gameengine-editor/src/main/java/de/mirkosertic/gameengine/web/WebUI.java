package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.teavm.*;
import de.mirkosertic.gameengine.web.github.*;
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