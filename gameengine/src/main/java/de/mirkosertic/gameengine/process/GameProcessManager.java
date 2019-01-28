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

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameSystem;
import de.mirkosertic.gameengine.core.GameSystemWork;
import de.mirkosertic.gameengine.core.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameProcessManager implements GameSystem {

    private final Logger logger;

    private GameProcess[] runningProcesses;
    private final Set<GameProcess> killedProcesses;

    GameProcessManager(Logger aLogger) {
        logger = aLogger;
        runningProcesses = new GameProcess[0];
        killedProcesses = new HashSet<>();
    }

    void start(GameProcess aProcess) {
        logger.info("Starting process of type " + aProcess.getClass().getName());

        List<GameProcess> theProcesses = ArrayUtils.asList(runningProcesses);
        theProcesses.add(aProcess);
        runningProcesses = theProcesses.toArray(new GameProcess[theProcesses.size()]);
        aProcess.started();
    }

    void kill(GameProcess aProcess) {
        logger.info("Killing process of type " + aProcess.getClass().getName());

        killedProcesses.add(aProcess);
        aProcess.killed();
    }

    void killProcessesForInstance(GameObjectInstance aInstance) {
        for (GameProcess theProcess : runningProcesses) {
            if (theProcess.affectsInstance(aInstance)) {
                kill(theProcess);
            }
        }
    }

    void instanceRemovedFromScene(GameObjectInstance aInstance) {
        killProcessesForInstance(aInstance);
    }

    @Override
    public GameSystemWork proceedGame(final long aTotalTicks, final long aGameTime, final long aElapsedTime) {

        return new GameSystemWork() {
            @Override
            public void runInFrame() {

                List<GameProcess> theNewChildProcesses = new ArrayList<>();

                for (GameProcess theProcess : runningProcesses) {

                    logger.info("Running process of type " + theProcess.getClass().getName() + " with time " + aElapsedTime);

                    GameProcess.ProceedResult theResult = theProcess.proceedGame(aGameTime, aElapsedTime);
                    switch (theResult) {
                    case CONTINUE_RUNNING:
                        // The process wants to continue, so we do it
                        break;
                    case STOPPED:
                        // The process wants to be stopped, so we will help him
                        killedProcesses.add(theProcess);
                        GameProcess theChildProcess = theProcess.getChildProcess();
                        if (theChildProcess != null) {
                            // There is a child process attached, so start with this one
                            theNewChildProcesses.add(theChildProcess);
                        }
                        break;
                    }
                }

                if (!theNewChildProcesses.isEmpty()) {
                    for (GameProcess theChildProcess : theNewChildProcesses) {
                        start(theChildProcess);
                    }
                }

                if (killedProcesses.size() > 0) {
                    List<GameProcess> theRunningProcesses = ArrayUtils.asList(runningProcesses);
                    theRunningProcesses.removeAll(killedProcesses);
                    runningProcesses = theRunningProcesses.toArray(new GameProcess[theRunningProcesses.size()]);
                    killedProcesses.clear();
                }
            }

            @Override
            public void runAfterFrame() {
            }
        };
   }
}