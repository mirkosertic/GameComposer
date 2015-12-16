package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;

import org.teavm.jso.ajax.ReadyStateChangeHandler;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.json.JSON;

import java.util.Map;

public class TeaVMGameSceneLoader {

    public interface GameSceneLoadedListener {
        void onGameSceneLoaded(GameScene aScene);
        void onGameSceneLoadedError(Throwable aThrowable);
    }

    private final GameSceneLoadedListener listener;
    private final AbstractGameRuntimeFactory runtimeFactory;

    public TeaVMGameSceneLoader(GameSceneLoadedListener aListener, AbstractGameRuntimeFactory aRuntimeFactory) {
        listener = aListener;
        runtimeFactory = aRuntimeFactory;
    }

    public void loadFromServer(final Game aGame, String aSceneName, final TeaVMGameResourceLoader aResourceLoader) {
        final XMLHttpRequest theRequest = XMLHttpRequest.create();
        theRequest.overrideMimeType("text/plain");
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
        Map<String, Object> theResult = new TeaVMMap<>((TeaVMMap.JSDelegate) JSON.parse(aResponse).cast());
        return GameScene.deserialize(aGame, runtimeFactory.create(aResourceLoader, new TeaVMGameSoundSystemFactory()), theResult);
    }
}