package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameRuntimeFactory;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGenericPlayer;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLCanvasElement;

public class WebUI {

    private static final Window window = Window.current();

    public static void main(String[] args) {
        if (window.getLocation().getPathName().endsWith("/index.html")) {
            Editor theEditor = new Editor();
            theEditor.boot();
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