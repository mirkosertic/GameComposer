package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameScene;

import org.teavm.dom.browser.Window;
import org.teavm.dom.html.HTMLDocument;
import org.teavm.dom.html.HTMLElement;
import org.teavm.jso.JS;

public class TeaVMRenderer {

    private static final Window window = (Window) JS.getGlobal();
    private static final HTMLDocument document = window.getDocument();

    public static void main(String[] args) {
        TeaVMRenderer theRenderer = new TeaVMRenderer();
        theRenderer.boot();
    }

    private TeaVMRenderer() {
    }

    public void boot() {
        GameLoopFactory theFactory = new GameLoopFactory();

        new TeaVMGameLoader(new TeaVMGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                HTMLElement div = document.createElement("div");
                div.appendChild(document.createTextNode("Loaded game"));
                document.getBody().appendChild(div);

                gameLoaded(aGame);
            }

            @Override
            public void onGameLoadedError(Throwable aThrowable) {
                HTMLElement div = document.createElement("div");
                div.appendChild(document.createTextNode("Error " + aThrowable.getMessage()));
                document.getBody().appendChild(div);
            }
        }, window).loadFromServer();
    }

    private void gameLoaded(Game aGame) {
        loadScene(aGame.defaultSceneProperty().get());
    }

    private void loadScene(final String aSceneName) {
        new TeaVMGameSceneLoader(new TeaVMGameSceneLoader.GameSceneLoadedListener() {
            @Override
            public void onGameSceneLoaded(GameScene aScene) {
                HTMLElement div = document.createElement("div");
                div.appendChild(document.createTextNode("Loaded scene " + aScene.nameProperty().get()));
                document.getBody().appendChild(div);
            }

            @Override
            public void onGameSceneLoadedError(Throwable aThrowable) {

            }
        }, new TeaVMGameRuntimeFactory(), window).loadFromServer(aSceneName, new TeaVMGameResourceLoader());
    }
}
