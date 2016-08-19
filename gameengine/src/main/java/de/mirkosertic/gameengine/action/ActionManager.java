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

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSystem;
import de.mirkosertic.gameengine.core.GameSystemWork;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.process.InvokeActionProcess;
import de.mirkosertic.gameengine.process.StartProcess;

public class ActionManager implements GameSystem {

    private final GameScene scene;

    ActionManager(GameScene aScene) {
        scene = aScene;
    }

    @Override
    public GameSystemWork proceedGame(final long aTotalTicks, final long aGameTime, final long aElapsedTime) {
        return new GameSystemWork() {
            @Override
            public void runInFrame() {
                scene.getRuntime().getEventManager().fire(new SystemTick(aTotalTicks, aGameTime, aElapsedTime));
            }

            @Override
            public void runAfterFrame() {
            }
        };
    }

    void onEvent(GameEvent aEvent) {
        for (EventSheet theSheet : scene.getEventSheets()) {
            for (GameRule theRule : theSheet.getRules()) {
                if (!theRule.conditionProperty().isNull()) {
                    ConditionResult theResult = theRule.conditionProperty().get().appliesTo(scene, aEvent);
                    if (theResult.isConditionTrue()) {
                        Action[] theActions = theRule.getActions();
                        if (theActions.length > 0) {
                            // Build a process tree and run it
                            InvokeActionProcess theRootProcess = new InvokeActionProcess(scene, theResult, theActions[0]);
                            InvokeActionProcess theChildProcess = theRootProcess;
                            for (int i=1;i<theActions.length;i++) {
                                InvokeActionProcess theNewChildProcess = new InvokeActionProcess(scene, theResult, theActions[i]);
                                theChildProcess.setChildProcess(theNewChildProcess);
                                theChildProcess = theNewChildProcess;
                            }
                            scene.getRuntime().getEventManager().fire(new StartProcess(theRootProcess));
                        }
                    }
                }
            }
        }
    }
}