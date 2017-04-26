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
package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromScene;
import de.mirkosertic.gameengine.core.Logger;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;

public class GameProcessManagerFactory {

    public GameProcessManager create(Logger aLogger, GameEventManager aEventManager) {
        final GameProcessManager theManager = new GameProcessManager(aLogger);
        aEventManager.register(null, StartProcess.TYPE, new GameEventListener<StartProcess>() {
            @Override
            public void handleGameEvent(StartProcess aEvent) {
                theManager.start(aEvent.process);
            }
        });
        aEventManager.register(null, KillProcessesForInstance.TYPE, new GameEventListener<KillProcessesForInstance>() {
            @Override
            public void handleGameEvent(KillProcessesForInstance aEvent) {
                theManager.killProcessesForInstance(aEvent.instance);
            }
        });
        aEventManager.register(null, GameObjectInstanceRemovedFromScene.TYPE, new GameEventListener<GameObjectInstanceRemovedFromScene>() {
            @Override
            public void handleGameEvent(GameObjectInstanceRemovedFromScene aEvent) {
                theManager.instanceRemovedFromScene(aEvent.instance);
            }
        });
        return theManager;
    }
}
