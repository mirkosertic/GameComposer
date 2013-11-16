package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.core.GameSystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameProcessManager implements GameSystem {

    private GameProcess[] runningProcesses;
    private final Set<GameProcess> killedProcesses;
    private long processesAmountOfTime;

    GameProcessManager() {
        runningProcesses = new GameProcess[0];
        killedProcesses = new HashSet<GameProcess>();
        processesAmountOfTime = 0;
    }

    void start(GameProcess aProcess) {
        List<GameProcess> theProcesses = ArrayUtils.asList(runningProcesses);
        theProcesses.add(aProcess);
        runningProcesses = theProcesses.toArray(new GameProcess[theProcesses.size()]);
        aProcess.started(this);
    }

    void kill(GameProcess aProcess) {
        killedProcesses.add(aProcess);
        aProcess.killed();
    }

    public void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        processesAmountOfTime += aElapsedTimeSinceLastLoop;
        // We limit the physics system to 60 frames / second, or we are getting strange results
        if (processesAmountOfTime > 8) {

            for (GameProcess theProcess : runningProcesses) {
                theProcess.proceedGame(aGameTime, processesAmountOfTime);
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