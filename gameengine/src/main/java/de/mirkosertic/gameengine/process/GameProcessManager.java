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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameProcessManager implements GameSystem {

    private GameProcess[] runningProcesses;
    private final Set<GameProcess> killedProcesses;
    private long processesAmountOfTime;

    GameProcessManager() {
        runningProcesses = new GameProcess[0];
        killedProcesses = new HashSet<>();
        processesAmountOfTime = 0;
    }

    void start(GameProcess aProcess) {
        List<GameProcess> theProcesses = ArrayUtils.asList(runningProcesses);
        theProcesses.add(aProcess);
        runningProcesses = theProcesses.toArray(new GameProcess[theProcesses.size()]);
        aProcess.started();
    }

    void kill(GameProcess aProcess) {
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
                processesAmountOfTime += aElapsedTime;
                // We limit the physics system to 60 frames / second, or we are getting strange results
                if (processesAmountOfTime > 8) {

                    List<GameProcess> theNewChildProcesses = new ArrayList<>();

                    for (GameProcess theProcess : runningProcesses) {
                        GameProcess.ProceedResult theResult = theProcess.proceedGame(aGameTime, processesAmountOfTime);
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
                    processesAmountOfTime = 0;
                }
            }

            @Override
            public void runAfterFrame() {

            }
        };
   }
}