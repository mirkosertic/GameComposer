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
package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.RuntimeStatistics;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetworkGameView implements GameView, GameEventListener<GameEvent> {

    private final List<DistributableEvent> eventsDuringLastLoopCycle;
    private final NetworkConnector networkConnector;
    private final EventInterpreter eventInterpreter;

    NetworkGameView(NetworkConnector aNetworkConnector, EventInterpreter aEventInterpreter) {
        eventsDuringLastLoopCycle = new ArrayList<>();
        networkConnector = aNetworkConnector;
        eventInterpreter = aEventInterpreter;
    }

    @Override
    public void handleGameEvent(GameEvent aEvent) {
        if (aEvent instanceof DistributableEvent) {
            eventsDuringLastLoopCycle.add((DistributableEvent) aEvent);
        }
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene, RuntimeStatistics aStatistics) {
        if (!eventsDuringLastLoopCycle.isEmpty()) {
            List<Map<String, Object>> theEventsToSend = new ArrayList<>();
            for (DistributableEvent theEvent : eventsDuringLastLoopCycle) {
                aStatistics.incrementNumberOfNetworkeventsSend();
                theEventsToSend.add(theEvent.serialize());
            }
            networkConnector.send(theEventsToSend);
            eventsDuringLastLoopCycle.clear();
        }

        // Tricky part: the EventInterpreter might also trigger events as
        // it changes the game scene state.
        List<Map<String, Object>> theReveived = networkConnector.receive();
        if (!theReveived.isEmpty()) {
            for (Map<String, Object> theSingleEvent : theReveived) {
                aStatistics.incrementNumberOfNetworkeventsReceived();
                eventInterpreter.process(theSingleEvent, aScene);
            }
        }

        // Clear every event, also the one which where caused by the event interpreter
        List<DistributableEvent> theNewEvents = new ArrayList<>(eventsDuringLastLoopCycle);
        eventsDuringLastLoopCycle.clear();
        // We keep only the forced events in our queue, so they are delivered during the next game loop cycle
        for (DistributableEvent theEvent : theNewEvents) {
            if (theEvent.isForced()) {
                eventsDuringLastLoopCycle.add(theEvent);
            }
        }
    }

    @Override
    public GestureDetector getGestureDetector() {
        throw new IllegalArgumentException("Not implemented, NetworkViews to not have a gesture detector");
    }
}