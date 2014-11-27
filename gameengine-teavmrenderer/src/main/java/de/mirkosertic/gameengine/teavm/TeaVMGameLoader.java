package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.teavm.json.JSONAPI;
import de.mirkosertic.gameengine.teavm.json.JSONAPIProvider;
import de.mirkosertic.gameengine.teavm.json.JSONMap;

import org.teavm.dom.ajax.ReadyStateChangeHandler;
import org.teavm.dom.ajax.XMLHttpRequest;
import org.teavm.dom.browser.Window;
import org.teavm.jso.JS;

import java.util.Map;

public class TeaVMGameLoader {

    public static interface GameLoadedListener {
        void onGameLoaded(Game aGame);
        void onGameLoadedError(Throwable aThrowable);
    }

    private final GameLoadedListener listener;
    private final Window window;

    public TeaVMGameLoader(GameLoadedListener aListener, Window aWindow) {
        listener = aListener;
        window = aWindow;
    }

    public void loadFromServer() {
        final XMLHttpRequest theRequest = window.createXMLHttpRequest();
        theRequest.open("GET", "game.json");
        theRequest.setOnReadyStateChange(new ReadyStateChangeHandler() {
            @Override
            public void stateChanged() {
                switch (theRequest.getReadyState()) {
                    default:
                        break;
                    case XMLHttpRequest.DONE:
                        listener.onGameLoaded(parse(theRequest.getResponseText()));
                }
            }
        });
        theRequest.send();
    }

    private Game parse(String aResponse) {
        JSONAPI theAPI = ((JSONAPIProvider) JS.getGlobal()).getJSONAPI();
        Map<String, Object> theResult = new JSONMap(theAPI.parse(aResponse));
        return Game.deserialize(theResult);
    }
}