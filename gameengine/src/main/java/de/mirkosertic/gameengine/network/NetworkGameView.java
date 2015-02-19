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