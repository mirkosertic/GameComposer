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
package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.Game;

import java.util.HashMap;
import java.util.Map;

import com.dragome.web.services.RequestExecutorImpl;
import com.dragome.services.ServiceLocator;
import com.dragome.services.interfaces.AsyncCallback;

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
