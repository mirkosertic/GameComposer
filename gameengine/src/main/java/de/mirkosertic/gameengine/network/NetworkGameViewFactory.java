package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventManager;

public class NetworkGameViewFactory {

    private final NetworkConnector networkConnector;
    private final EventInterpreter eventInterpreter;

    public NetworkGameViewFactory(NetworkConnector aConnector, EventInterpreter aEventInterpreter) {
        networkConnector = aConnector;
        eventInterpreter = aEventInterpreter;
    }

    public NetworkGameView createNetworkViewFor(GameEventManager aEventManager) {
        NetworkGameView theView = new NetworkGameView(networkConnector, eventInterpreter);
        aEventManager.register(null, GameEvent.class, theView);
        return theView;
    }
}