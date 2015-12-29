package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameSystem;

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
    public void proceedGame(long aTotalTicks, long aGameTime, long aElapsedTime) {

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
}