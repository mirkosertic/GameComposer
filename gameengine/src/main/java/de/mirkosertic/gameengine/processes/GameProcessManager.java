package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.core.GameSystem;

import java.util.HashSet;
import java.util.Set;

public class GameProcessManager implements GameSystem {

    private Set<GameProcess> runningProcesses;
    private Set<GameProcess> killedProcesses;
    private long processesAmountOfTime;

    GameProcessManager() {
        runningProcesses = new HashSet<GameProcess>();
        killedProcesses = new HashSet<GameProcess>();
        processesAmountOfTime = 0;
    }

    void start(GameProcess aProcess) {
        runningProcesses.add(aProcess);
    }

    void kill(GameProcess aProcess) {
        killedProcesses.add(aProcess);
    }

    public void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        processesAmountOfTime += aElapsedTimeSinceLastLoop;
        // We limit the physics system to 60 frames / second, or we are getting strange results
        if (processesAmountOfTime > 8) {

            for (GameProcess theProcess : runningProcesses) {
                theProcess.proceedGame(aGameTime, processesAmountOfTime);
            }
            runningProcesses.removeAll(killedProcesses);
            killedProcesses.clear();
            processesAmountOfTime = 0;
        }
    }
}