package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.HashMap;
import java.util.Map;

import com.dragome.web.services.RequestExecutorImpl;
import com.dragome.services.ServiceLocator;
import com.dragome.services.interfaces.AsyncCallback;

class DragomeGameSceneLoader {

    public static interface GameSceneLoadedListener {
        void onGameSceneLoaded(GameScene aScene);
        void onGameSceneLoadedError();
    }

    private final GameSceneLoadedListener listener;
    private final AbstractGameRuntimeFactory runtimeFactory;

    public DragomeGameSceneLoader(GameSceneLoadedListener aListener, AbstractGameRuntimeFactory aRuntimeFactory) {
        listener = aListener;
        runtimeFactory = aRuntimeFactory;
    }

    public void loadFromServer(Game aGame, String aSceneName, final DragomeGameResourceLoader aResourceLoader) {
        RequestExecutorImpl.executeHttpRequest(true, aSceneName+"/scene.json", new HashMap<>(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                listener.onGameSceneLoaded(parse(aGame, result, aResourceLoader));
            }

            @Override
            public void onError() {
                listener.onGameSceneLoadedError();
            }
        }, false, true);
    }

    private GameScene parse(Game aGame, String aResponse, DragomeGameResourceLoader aResourceLoader) {
        Map<String, Object> theResult = (Map<String, Object>)
                ServiceLocator.getInstance().getSerializationService().deserialize(aResponse);
        return GameScene.deserialize(aGame, runtimeFactory.create(aResourceLoader, new DragomeGameSoundSystemFactory()), theResult);
    }
}