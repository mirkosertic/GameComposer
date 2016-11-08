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