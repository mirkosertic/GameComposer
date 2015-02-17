package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.RuntimeStatistics;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.RemoteEvent;

import java.util.ArrayList;
import java.util.List;

public class NetworkGameView implements GameView, GameEventListener<GameEvent> {

    private final List<RemoteEvent> eventsDuringLastLoopCycle;
    private final NetworkConnector networkConnector;

    NetworkGameView(NetworkConnector aNetworkConnector) {
        eventsDuringLastLoopCycle = new ArrayList<>();
        networkConnector = aNetworkConnector;
    }

    @Override
    public void handleGameEvent(GameEvent aEvent) {
        if (aEvent instanceof RemoteEvent) {
            System.out.println("Received event " + aEvent);
            eventsDuringLastLoopCycle.add((RemoteEvent) aEvent);
        }
    }

    @Override
    public void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene, RuntimeStatistics aStatistics) {
        eventsDuringLastLoopCycle.clear();
    }

    @Override
    public GestureDetector getGestureDetector() {
        throw new IllegalArgumentException("Not implemented, NetworkViews to not have a gesture detector");
    }
}