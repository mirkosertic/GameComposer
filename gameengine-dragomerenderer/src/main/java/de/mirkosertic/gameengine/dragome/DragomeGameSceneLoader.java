package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.HashMap;
import java.util.Map;

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

    public void loadFromServer(String aSceneName, final DragomeGameResourceLoader aResourceLoader) {
        ServiceLocator.getInstance().getRequestExecutor().executeAsynchronousRequest(aSceneName+"/scene.json", new HashMap<>(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                listener.onGameSceneLoaded(parse(result, aResourceLoader));
            }

            @Override
            public void onError() {
                listener.onGameSceneLoadedError();
            }
        });
    }

    private GameScene parse(String aResponse, DragomeGameResourceLoader aResourceLoader) {
        Map<String, Object> theResult = (Map<String, Object>)
                ServiceLocator.getInstance().getSerializationService().deserialize(aResponse);
        return GameScene.deserialize(runtimeFactory.create(aResourceLoader, new DragomeGameSoundSystemFactory()), theResult);
    }
}
