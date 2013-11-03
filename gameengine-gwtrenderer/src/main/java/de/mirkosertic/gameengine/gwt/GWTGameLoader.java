package de.mirkosertic.gameengine.gwt;

import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import de.mirkosertic.gameengine.core.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GWTGameLoader {
    
    public static interface GameLoadedListener {
        void onGameLoaded(Game aGame);
        void onGameLoadedError(Throwable aThrowable);
    }
    
    private GameLoadedListener listener;

    public GWTGameLoader(GameLoadedListener aListener) {
        listener = aListener;    
    }

    public void loadFromServer() {
        RequestBuilder theBuilder = new RequestBuilder(RequestBuilder.GET, "game.json");
        try {
            theBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request aRequest, Response aResponse) {
                    listener.onGameLoaded(parse(aResponse));
                }

                @Override
                public void onError(Request aRequest, Throwable aThrowable) {
                    listener.onGameLoadedError(aThrowable);
                }
            });
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private Game parse(Response aResponse) {
        JSONValue theJSONParsed = JSONParser.parseStrict(aResponse.getText());
        Map<String, Object> theResult = JSONUtils.toMap(theJSONParsed);
        return Game.deserialize(theResult);
    }
}
