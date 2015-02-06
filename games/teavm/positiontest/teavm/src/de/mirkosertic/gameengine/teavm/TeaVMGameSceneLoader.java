package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.teavm.json.JSONAPI;
import de.mirkosertic.gameengine.teavm.json.JSONAPIProvider;
import de.mirkosertic.gameengine.teavm.json.JSONMap;

import org.teavm.dom.ajax.ReadyStateChangeHandler;
import org.teavm.dom.ajax.XMLHttpRequest;
import org.teavm.dom.browser.Window;
import org.teavm.jso.JS;

import java.util.Map;

public class TeaVMGameSceneLoader {

    public static interface GameSceneLoadedListener {
        void onGameSceneLoaded(GameScene aScene);
        void onGameSceneLoadedError(Throwable aThrowable);
    }

    private final GameSceneLoadedListener listener;
    private final AbstractGameRuntimeFactory runtimeFactory;
    private final Window window;

    public TeaVMGameSceneLoader(GameSceneLoadedListener aListener, AbstractGameRuntimeFactory aRuntimeFactory, Window aWindow) {
        listener = aListener;
        runtimeFactory = aRuntimeFactory;
        window = aWindow;
    }

    public void loadFromServer(final Game aGame, String aSceneName, final TeaVMGameResourceLoader aResourceLoader) {
        final XMLHttpRequest theRequest = window.createXMLHttpRequest();
        theRequest.open("GET", aSceneName+"/scene.json");
        theRequest.setOnReadyStateChange(new ReadyStateChangeHandler() {
            @Override
            public void stateChanged() {
                switch (theRequest.getReadyState()) {
                    default:
                        break;
                    case XMLHttpRequest.DONE:
                        listener.onGameSceneLoaded(parse(aGame, theRequest.getResponseText(), aResourceLoader));
                }
            }
        });
        theRequest.send();
    }

    private GameScene parse(Game aGame, String aResponse, TeaVMGameResourceLoader aResourceLoader) {
        JSONAPI theAPI = ((JSONAPIProvider) JS.getGlobal()).getJSONAPI();
        Map<String, Object> theResult = new JSONMap(theAPI.parse(aResponse));
        return GameScene.deserialize(aGame, runtimeFactory.create(aResourceLoader, new TeaVMGameSoundSystemFactory()), theResult);
    }
}