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
package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.GameEventType;

public class ActionManagerFactory {

    public ActionManager create(GameScene aScene, GameEventManager aEventManager) {
        final ActionManager theManager = new ActionManager(aScene);
        aEventManager.register(null, GameEventType.CATCH_ALL, new GameEventListener<GameEvent>() {
            @Override
            public void handleGameEvent(GameEvent aEvent) {
                theManager.onEvent(aEvent);
            }
        });
        return theManager;
    }
}
