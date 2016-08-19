/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.core.Game;

import java.util.Map;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

class GWTGameLoader {
    
    public static interface GameLoadedListener {
        void onGameLoaded(Game aGame);
        void onGameLoadedError(Throwable aThrowable);
    }
    
    private final GameLoadedListener listener;

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
