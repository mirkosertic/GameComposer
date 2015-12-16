package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.Game;

import org.teavm.jso.ajax.ReadyStateChangeHandler;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.json.JSON;

import java.util.Map;

public class TeaVMGameLoader {

    public interface GameLoadedListener {
        void onGameLoaded(Game aGame);
        void onGameLoadedError(Throwable aThrowable);
    }

    private final GameLoadedListener listener;

    public TeaVMGameLoader(GameLoadedListener aListener) {
        listener = aListener;
    }

    public void loadFromServer() {
        final XMLHttpRequest theRequest = XMLHttpRequest.create();
        theRequest.overrideMimeType("text/plain");
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
        Map<String, Object> theResult = JSON.parse(aResponse).cast();
        return Game.deserialize(theResult);
    }
}