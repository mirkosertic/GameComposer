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

    NetworkGameView(NetworkConnector aNetworkConnector) {
        eventsDuringLastLoopCycle = new ArrayList<>();
        networkConnector = aNetworkConnector;
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
            System.out.println("Sending " + eventsDuringLastLoopCycle.size()+" events");
            List<Map<String, Object>> theEventsToSend = new ArrayList<>();
            for (DistributableEvent theEvent : eventsDuringLastLoopCycle) {
                theEventsToSend.add(theEvent.serialize());
            }
            networkConnector.send(theEventsToSend);
        }
        List<Map<String, Object>> theReveived = networkConnector.receive();
        if (!theReveived.isEmpty()) {
            System.out.println("Received " + theReveived);
        }
        eventsDuringLastLoopCycle.clear();
    }

    @Override
    public GestureDetector getGestureDetector() {
        throw new IllegalArgumentException("Not implemented, NetworkViews to not have a gesture detector");
    }
}