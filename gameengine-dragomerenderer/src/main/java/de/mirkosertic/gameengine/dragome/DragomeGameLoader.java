package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.Game;

import java.util.HashMap;
import java.util.Map;
import com.dragome.services.ServiceLocator;
import com.dragome.services.interfaces.AsyncCallback;
import com.dragome.web.services.RequestExecutorImpl;

class DragomeGameLoader {

    public interface GameLoadedListener {
        void onGameLoaded(Game aGame);

        void onGameLoadedError();
    }

    private final GameLoadedListener listener;

    public DragomeGameLoader(GameLoadedListener aListener) {
        listener = aListener;
    }

    public void loadFromServer() {
        RequestExecutorImpl.executeHttpRequest(true, "game.json", new HashMap<>(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                listener.onGameLoaded(parse(result));
            }

            @Override
            public void onError() {
                listener.onGameLoadedError();

            }
        }, false, true);
    }

    private Game parse(String aResponse) {
        Map<String, Object> theResult = (Map<String, Object>)
                ServiceLocator.getInstance().getSerializationService().deserialize(aResponse);
        return Game.deserialize(theResult);
    }
}
