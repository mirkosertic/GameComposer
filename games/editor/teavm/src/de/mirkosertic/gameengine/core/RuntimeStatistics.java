package de.mirkosertic.gameengine.core;

public class RuntimeStatistics {

    private final long startTime;
    private long numberTicks;
    private long cycleStart;
    private long averageTimePerLoopCycle;
    private long numberOfNetworkeventsSend;
    private long numberOfNetworkeventsReceived;

    public RuntimeStatistics() {
        startTime = System.currentTimeMillis();
    }

    public long incrementTicks() {
        numberTicks++;
        return numberTicks;
    }

    public long incrementNumberOfNetworkeventsSend() {
        numberOfNetworkeventsSend++;
        return numberOfNetworkeventsSend;
    }

    public long incrementNumberOfNetworkeventsReceived() {
        numberOfNetworkeventsReceived++;
        return numberOfNetworkeventsReceived;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getNumberTicks() {
        return numberTicks;
    }

    public void beginGameLoop() {
        cycleStart = System.currentTimeMillis();
    }

    public void endGameLoop() {
        averageTimePerLoopCycle = (averageTimePerLoopCycle + System.currentTimeMillis() - cycleStart) / 2;
    }

    public long getAverageTimePerLoopCycle() {
        return averageTimePerLoopCycle;
    }
}